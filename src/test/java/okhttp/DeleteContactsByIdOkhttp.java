package okhttp;

import com.google.gson.Gson;
import dto.ContactDto;
import dto.ErrorDto;
import dto.MessageDto;
import okhttp3.*;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Random;

public class DeleteContactsByIdOkhttp {
    String token = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoibG9saWtAbWFpbC5ydSIsImlzcyI6IlJlZ3VsYWl0IiwiZXhwIjoxNzgzMTYwOTkwLCJpYXQiOjE3ODI1NjA5OTB9.-G9ECquQI9FB4rYnqGhjbMoxuflCAkZfbTwfZFFxJaY";
    Gson gson = new Gson();
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json");
    String id;

    @BeforeMethod
    public void preCondition() throws IOException {
        //create contact
        int i = new Random().nextInt(1000)+1000;
        ContactDto contactDto = ContactDto.builder()
                .name("Maya")
                .lastName("Dow")
                .email("maya" + i + "@gmail.com")
                .phone("1234556"+i)
                .address("Haifa")
                .description("Sister")
                .build();

        RequestBody body = RequestBody.create(gson.toJson(contactDto),JSON);
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts")
                .post(body)
                .addHeader("Authorization",token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertTrue(response.isSuccessful());
        MessageDto messageDto = gson.fromJson(response.body().string(), MessageDto.class);
        //get id from "message": "Contact was added! ID: a7809d99-68b8-46f7-8a81-f281e96883b9"
        String message = messageDto.getMessage();
        System.out.println(message);
        String [] all =  message.split(": ");
        id = all[1];
        System.out.println(id);
    }

    @Test
    public void deleteContactByIdSuccess() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/"+id)
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),200);

        MessageDto dto = gson.fromJson(response.body().string(), MessageDto.class);
        System.out.println(dto.getMessage());
        Assert.assertEquals(dto.getMessage(),"Contact was deleted!");
    }
    @Test
    public void deleteContactByIWrongId() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/123")
                .delete()
                .addHeader("Authorization", token)
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),400);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getError());
        System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getStatus(),400);
        Assert.assertEquals(errorDto.getError(),"Bad Request");
        Assert.assertEquals(errorDto.getMessage(), "Contact with id: 123 not found in your contacts!");
    }
    @Test
    public void deleteContactByIWrongToken() throws IOException {
        Request request = new Request.Builder()
                .url("https://contactapp-telran-backend.herokuapp.com/v1/contacts/+id")
                .delete()
                .addHeader("Authorization", "frghjkiuygtbhn")
                .build();
        Response response = client.newCall(request).execute();
        Assert.assertEquals(response.code(),401);

        ErrorDto errorDto = gson.fromJson(response.body().string(), ErrorDto.class);
        System.out.println(errorDto.getError());
        System.out.println(errorDto.getMessage());
        Assert.assertEquals(errorDto.getStatus(),401);
        Assert.assertEquals(errorDto.getError(),"Unauthorized");
        Assert.assertEquals(errorDto.getMessage(), "JWT strings must contain exactly 2 period characters. Found: 0");
    }
}

//8d17b097-788c-4512-92e6-708ea8c9335f
//Cheyenne.McGlynn@gmail.com
//3523671014932
//        =============
//        02dc9177-c9fc-4d37-9db1-86ec5a1d3805
//Eula18@hotmail.com
//3523671014359
//        =============
//        83fd0458-8784-40cc-ab11-b600a8eea8b1
//Delia_Lockman@yahoo.com
//3523671014967
//        =============
//b17a2002-ee40-4924-be9b-b2ad86105072
//Grayson_Bernier@yahoo.com
//3523671014855
//        =============
//        9c5acfe7-1914-4430-9e31-3e03f9886751
//Angus57@yahoo.com
//3523671014927
//        =============
//        75703ee6-dc18-4d05-9aab-3334019d1509
//Lucius_Cormier95@yahoo.com
//3523671014172
//        =============
//b24d7273-3f56-40c1-bc4f-b7c51d2fc558
//Stanford.Powlowski@yahoo.com
//3523671014345
//        =============
//a392f3d7-c0f8-4346-8533-2bec4c27db06
//Orie.Hermiston@gmail.com
//3523671014509
//        =============
//        62d52888-b1a5-4fdd-9d69-e5627a8a7f93
//Elenor.Wisoky14@hotmail.com
//3523671014942
//        =============
//bd9f0aa6-010b-4c3c-9d80-2ad0af9b128a
//Elna_Bayer@yahoo.com
//3523671014787
//        =============
//b4c85511-76ff-45c4-9b9e-58690ea37e58
//Ulises_Auer16@gmail.com
//3523671014148
//        =============
//        35862c10-7324-49c7-b9b6-111143b69b17
//Philip.Lesch60@yahoo.com
//352367101451
//        =============
//dc52e314-3ba4-4915-a579-f8643f65af98
//Edna_Reichert@hotmail.com
//3523671014780
//        =============
