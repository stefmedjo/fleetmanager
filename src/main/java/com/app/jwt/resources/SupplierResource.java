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

import com.app.jwt.dto.SupplierDTO;
import com.app.jwt.models.Supplier;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/suppliers")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SupplierResource {

    @POST
    @Transactional
    public Response create(SupplierDTO dto){
        if(this.isDTOValid(dto)){
            if(this.isCodeUnique(dto.code) == null){
                Supplier entity = new Supplier();
                entity.address = dto.address;
                entity.code = dto.code;
                entity.name = dto.name;
                entity.phone = dto.phone;
                entity.persist();
                return Response.status(Status.CREATED).entity("Item created successfully").build();
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Code already used").build();
            }
        }else{
            return Response.status(Status.BAD_REQUEST).encoding("Invalid Credentials.").build();
        }
    }

    @GET
    public Response read(@PathParam("id") Long id){
        Supplier entity = Supplier.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    public Response update(@PathParam("id") Long id, SupplierDTO dto){
        Supplier entity = Supplier.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            if(this.isDTOValid(dto)){
                if(this.isCodeUnique(dto.code) == null){
                    entity.address = dto.address;
                    entity.code = dto.code;
                    entity.name = dto.name;
                    entity.phone = dto.phone;
                    entity.persist();
                    return Response.status(Status.CREATED).entity("Item created successfully").build();
                }else{
                    return Response.status(Status.BAD_REQUEST).encoding("Code already used").build();
                }
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Invalid Credentials.").build();
            }
        }
    }

    @DELETE
    public Response delete(@PathParam("id") Long id){
        Supplier entity = Supplier.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Supplier.listAll()).build();
    }

    private boolean isDTOValid(SupplierDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            StringUtils.isNullOrEmpty(dto.name)
        ){
            res = false;
        }
        return res;
    }

    private Supplier isCodeUnique(String code){
        return Supplier.find("code", code).firstResult();
    }
    
}