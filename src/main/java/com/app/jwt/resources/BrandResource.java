package com.app.jwt.resources;

import javax.annotation.security.RolesAllowed;
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

import com.app.jwt.dto.BrandDTO;
import com.app.jwt.models.Brand;
import com.app.jwt.utils.TokenUtils;

@Path("/api/v1/brands")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BrandResource {

    @POST
    @Transactional
    @RolesAllowed({ TokenUtils.ROLE_ADMIN })
    public Response create(BrandDTO brandDTO){
        if(!this.isDTOValid(brandDTO)){
            return Response.status(Status.BAD_REQUEST).build();
        }else{
            if(this.isCodeUnique(brandDTO.code) == null){
                Brand brand = new Brand();
                brand.code = brandDTO.code;
                brand.designation = brandDTO.designation;
                brand.persist();
                return Response.status(Status.CREATED).entity(brand).build();
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Code already used.").build();
            }
        }
    }

    @GET
    @Path("{id}")
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response read(@PathParam("id") Long id){
        Brand brand = Brand.findById(id);
        if(brand == null){
            return Response.status(Status.NOT_FOUND).encoding("Data not found in database.").build();
        }else{
            return Response.status(Status.OK).entity(brand).build();
        }
    }
    
    @PUT
    @Path("{id}")
    @RolesAllowed({ TokenUtils.ROLE_ADMIN })
    public Response update(@PathParam("id") Long id, BrandDTO brandDTO){
        if(!this.isDTOValid(brandDTO)){
            return Response.status(Status.BAD_REQUEST).build();
        }else{
            Brand brand = Brand.findById(id);
            Brand withCodeBrand = this.isCodeUnique(brandDTO.code);
            if(withCodeBrand == null || withCodeBrand == brand){
                brand.code = brandDTO.code;
                brand.designation = brandDTO.designation;
                brand.persist();
                return Response.status(Status.CREATED).entity(brand).build();
            }else{
                return Response.status(Status.BAD_REQUEST).encoding("Code already used.").build();
            }
        }
    }

    @DELETE
    @Path("{id}")
    @RolesAllowed({ TokenUtils.ROLE_ADMIN })
    public Response delete(@PathParam("id") Long id){
        Brand brand = Brand.findById(id);
        if(brand == null){
            return Response.status(Status.NOT_FOUND).encoding("Impossible to find this data.").build();
        }else{
            brand.delete();
            return Response.status(Status.OK).build();
        }
    }

    @GET
    @RolesAllowed({ TokenUtils.ROLE_USER })
    public Response list(){
        return Response.status(Status.OK).entity(Brand.listAll()).build();
    }

    private boolean isDTOValid(BrandDTO brandDTO){
        boolean res = true;
        if(brandDTO.code == null || brandDTO.designation == null || brandDTO.code.isEmpty() || brandDTO.designation.isEmpty()){
            res = false;
        }
        return res;
    }

    private Brand isCodeUnique(String code){
        return Brand.find("code", code).firstResult();
    }

}