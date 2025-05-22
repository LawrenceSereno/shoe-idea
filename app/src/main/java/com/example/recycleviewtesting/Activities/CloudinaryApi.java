package com.example.recycleviewtesting.Activities;

import com.cloudinary.Cloudinary;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryApi {
    public static Cloudinary cloudinary;

    public static void initializeCloudinary(){
        Map config = new HashMap();
        config.put("cloud_name","djoh0bfd5");
        config.put("api_key","787663864757948");
        config.put("api_secret","VmLQeDRlEBcWUEUa6I022wrEi1I");
        cloudinary = new Cloudinary(config);
    }
}
