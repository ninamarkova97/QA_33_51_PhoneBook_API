package okhttp;

import com.google.gson.Gson;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import dto.ErrorDto;
import lombok.ToString;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class RegistrationTestsOkhtpp {
    Gson gson = new Gson();
    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient();

    @Test
    public void registrationSuccessTest() throws IOException {
        Random random = new Random();
        int i = random.nextInt(1000)+1000;

        AuthRequestDto auth = AuthRequestDto.builder()
                .username("lolik"+i+"@mail.ru")
                .password("Lolik123!")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();

        Assert.assertTrue(response.isSuccessful());
        Assert.assertEquals(response.code(),200);

        String responseBody = response.body().string();
        AuthResponseDto responseDto =gson.fromJson(responseBody, AuthResponseDto.class);
        System.out.println(responseDto.getToken());

    }
    @Test
    public void registrationWrongEmail() throws IOException {
        Random random = new Random();
        int i = random.nextInt(1000) + 1000;

        AuthRequestDto auth = AuthRequestDto.builder()
                .username("lolikmail.ru")
                .password("Lolik123!")
                .build();

        RequestBody requestBody = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(),400);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getMessage().toString(), "{username=must be a well-formed email address}");
        Assert.assertEquals(errorDto.getPath(),"/v1/user/registration/usernamepassword");
    }
    @Test
    public void registrationWrongPassword() throws IOException {
        Random random = new Random();
        int i = random.nextInt(1000) + 1000;

        AuthRequestDto auth = AuthRequestDto.builder()
                .username("lolik@mail.ru")
                .password("Lolik123")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth), JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertFalse(response.isSuccessful());
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getMessage().toString());

        Assert.assertEquals(errorDto.getStatus(),400);
        Assert.assertEquals(errorDto.getError(), "Bad Request");

        Assert.assertEquals(errorDto.getMessage().toString(), "{password= At least 8 characters; Must contain at least 1 uppercase letter, 1 lowercase letter, and 1 number; Can contain special characters [@$#^&*!]}");
        Assert.assertEquals(errorDto.getPath(),"/v1/user/registration/usernamepassword");
    }
    @Test
    public void registrationTestRegisteredUser() throws IOException {
        AuthRequestDto auth = AuthRequestDto.builder()
                .username("lolik@mail.ru")
                .password("Lolik123!")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(auth),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/user/registration/usernamepassword")
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(), 409);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        Assert.assertEquals(errorDto.getStatus(), 409);
        System.out.println(errorDto);
        Assert.assertEquals(errorDto.getError(), "Conflict");
        Assert.assertEquals(errorDto.getMessage().toString(), "User already exists");

    }
}


