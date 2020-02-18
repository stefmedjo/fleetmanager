package com.app.jwt.resources;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.app.jwt.dto.ModelDTO;
import com.app.jwt.models.Brand;
import com.app.jwt.models.Model;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/models")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ModelResource {

    @POST
    @Transactional
    public Response create(ModelDTO modelDTO){
        if(this.isDTOValid(modelDTO)){
            if(this.isCodeUnique(modelDTO.code) == null){
                Brand brand = Brand.findById(modelDTO.brandId);
                if(brand == null){
                    return Response.status(Status.NOT_FOUND).encoding("Brand not found.").build();
                }else{
                    Model model = new Model();
                    model.code = modelDTO.code;
                    model.designation = modelDTO.designation;
                    model.brand = brand;
                    model.persist();
                    return Response.status(Status.CREATED).entity(model).build();
                }                
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Code already used.").build();
            }
        }else{
            return Response.status(Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("{id}")
    public Response read(@PathParam("id") Long id){
        Model entity = Model.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }
    
    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, ModelDTO modelDTO){
        Model entity = Model.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            if(this.isDTOValid(modelDTO)){
                if(this.isCodeUnique(modelDTO.code) == null || this.isCodeUnique(modelDTO.code) == entity){
                    Brand brand = Brand.findById(modelDTO.brandId);
                    if(brand == null){
                        return Response.status(Status.NOT_FOUND).encoding("Brand not found.").build();
                    }else{
                        entity.code = modelDTO.code;
                        entity.designation = modelDTO.designation;
                        entity.brand = brand;
                        entity.persist();
                        return Response.status(Status.CREATED).entity(entity).build();
                    }                
                }else{
                    return Response.status(Status.BAD_REQUEST).encoding("Code already used.").build();
                }
            }else{
                return Response.status(Status.BAD_REQUEST).build();
            }
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id){
        Model entity = Model.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            entity.delete();
            return Response.status(Status.OK).build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Model.listAll()).build();
    }

    private boolean isDTOValid(ModelDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            StringUtils.isNullOrEmpty(dto.designation)||
            dto.brandId == null
        ){
            res = false;
        }
        return res;
    }

    private Model isCodeUnique(String code){
        return Model.find("code", code).firstResult();
    }

}