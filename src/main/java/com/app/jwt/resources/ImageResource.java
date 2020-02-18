package com.app.jwt.resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.app.jwt.models.Image;
import com.app.jwt.models.Vehicle;

import org.apache.commons.io.IOUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;


@Path("/api/v1/images")
public class ImageResource {

    @ConfigProperty(name = "upload.vehicles.path")
    String _path;

    @POST
    public Response upload(MultipartFormDataInput input){
        String fileName = "";
        Vehicle vehicle = null;
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        

        List<InputPart> idInputParts = uploadForm.get("id");
        for(InputPart inputPart : idInputParts){
            try {
                
                vehicle = Vehicle.findById(Long.parseLong(inputPart.getBodyAsString())); 
                if(vehicle == null){
                    return Response.status(Status.BAD_REQUEST).entity("Impossible to find the vehicle.").build();
                }
                
            } catch (Exception e) {
                System.out.print(e.getMessage());
                return Response.status(Status.BAD_REQUEST).entity(e).build();
            }
        }
        
        List<InputPart> inputParts = uploadForm.get("file");
        for(InputPart inputPart : inputParts){
            try {
                MultivaluedMap<String,String> header = inputPart.getHeaders();
                fileName = getFileName(header);
                InputStream inputStream = inputPart.getBody(InputStream.class, null);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                fileName = _path + fileName;
                writeFile(bytes, fileName);;
                System.out.println("Done");
                System.out.println(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        
        Image entity = new Image();
        entity.path = fileName;
        entity.vehicle = vehicle;
        entity.persist();
        

        return Response.status(Status.OK).entity("entity").build();
    }

    private String getFileName(MultivaluedMap<String,String> header){
        String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
        for(String fileName : contentDisposition){
            if((fileName.trim().startsWith("filename"))){
                String[] name = fileName.split("=");
                String finalFileName = name[1].trim().replace("\"", "");
                return finalFileName;
            }
        }
        return "unknown";
    }

    private void writeFile(byte[] content,String filename) throws IOException{
        File file = new File(filename);
        if(!file.exists()){
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        fop.flush();
        fop.close();

    }

}