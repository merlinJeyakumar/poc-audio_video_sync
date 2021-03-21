package com.data.webservices;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class MultipartUtil {

    /* -------------------------------  Multipart Util method --------------------------------------- */
    @NonNull
    static RequestBody createPartFromString(String partValue) {
        /*return RequestBody.create(MediaType.parse("text/plain"), partValue);*/
        return RequestBody.create(MultipartBody.FORM, partValue);

    }

    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, String selectedImage) {
        File file = new File(selectedImage);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/octet-stream"),
                        file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }


    @NonNull
    public static MultipartBody.Part prepareFilePart(String partName, File file) {

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/octet-stream"),
                        file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public static MultipartBody.Part prepareFilePart(String partName, String fileName, byte[] toByteArray) {

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("application/octet-stream"),
                        toByteArray);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, fileName, requestFile);
    }
}
