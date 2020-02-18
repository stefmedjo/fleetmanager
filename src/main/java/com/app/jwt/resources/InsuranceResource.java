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

import com.app.jwt.dto.InsuranceDTO;
import com.app.jwt.models.Insurance;
import com.app.jwt.models.Supplier;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/Insurances")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InsuranceResource {

    @POST
    @Transactional
    public Response create(InsuranceDTO dto){
        if(this.isDTOValid(dto)){
            if(this.isCodeUnique(dto.code) == null){
                Supplier supplier = Supplier.findById(dto.supplierId);
                if(supplier == null){
                    return Response.status(Status.NOT_FOUND).entity("Impossible to find this insurance supplier.").build();
                }else{
                    Insurance entity = new Insurance();
                    entity.code = dto.code;
                    entity.designation = dto.designation;
                    entity.description = dto.description;
                    entity.supplier = supplier;
                    entity.persist();
                    return Response.status(Status.CREATED).entity("Insurance created successfully.").build();
                }
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Code already used.").build();
            }
        }else{
            return Response.status(Status.BAD_REQUEST).encoding("Invalid Credentials.").build();
        }
    }

    @GET
    public Response read(@PathParam("id") Long id){
        Insurance entity = Insurance.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    public Response update(@PathParam("id") Long id, InsuranceDTO dto){
        Insurance entity = Insurance.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            if(this.isDTOValid(dto)){
                if(this.isCodeUnique(dto.code) == null || this.isCodeUnique(dto.code) == entity){
                    Supplier supplier = Supplier.findById(dto.supplierId);
                    if(supplier == null){
                        return Response.status(Status.NOT_FOUND).entity("Impossible to find this insurance supplier.").build();
                    }else{
                        entity.code = dto.code;
                        entity.designation = dto.designation;
                        entity.description = dto.description;
                        entity.supplier = supplier;
                        entity.persist();
                        return Response.status(Status.CREATED).entity("Insurance created successfully.").build();
                    }
                }else{
                    return Response.status(Status.BAD_REQUEST).encoding("Code already used.").build();
                }
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Invalid Credentials.").build();
            }
        }
    }

    @DELETE
    public Response delete(@PathParam("id") Long id){
        Insurance entity = Insurance.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Insurance.listAll()).build();
    }

    private boolean isDTOValid(InsuranceDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            StringUtils.isNullOrEmpty(dto.designation) || 
            dto.supplierId == null
        ){
            res = false;
        }
        return res;
    }

    private Insurance isCodeUnique(String code){
        return Insurance.find("code", code).firstResult();
    }
    
}