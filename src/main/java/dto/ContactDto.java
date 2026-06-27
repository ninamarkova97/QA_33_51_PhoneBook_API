package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class ContactDto {

//    {
//        "contacts": [
//        {
//            "id": "string",
//                "name": "string",
//                "lastName": "string",
//                "email": "string",
//                "phone": "64450054395699",
//                "address": "string",
//                "description": "string"
//        }

    private String id;
    private  String name;
    private  String lastName;
    private  String email;
    private  String phone;
    private  String address;
    private  String description;
}
