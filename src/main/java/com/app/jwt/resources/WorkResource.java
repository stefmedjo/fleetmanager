package com.app.jwt.resources;

import java.time.LocalDateTime;

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

import com.app.jwt.dto.WorkDTO;
import com.app.jwt.models.Vehicle;
import com.app.jwt.models.Work;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/works")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkResource {

    @POST
    @Transactional
    public Response create(WorkDTO dto){
        if(this.isDTOValid(dto)){
            if(this.isCodeUnique(dto.code) == null){
                Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                if(vehicle == null){
                    return Response.status(Status.NOT_FOUND).encoding("Vehicle not found.").build();
                }else{
                    Work entity = new Work();
                    entity.code = dto.code;
                    entity.designation = dto.designation;
                    entity.vehicle = vehicle;
                    entity.realizedOn = LocalDateTime.parse(dto.realizedOn);
                    entity.expectedOn = LocalDateTime.parse(dto.expectedOn);
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

    @GET
    @Path("{id}")
    public Response read(@PathParam("id") Long id){
        Work entity = Work.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, WorkDTO dto){
        Work entity = Work.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            if(this.isDTOValid(dto)){
                if(this.isCodeUnique(dto.code) == null){
                    Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                    if(vehicle == null){
                        return Response.status(Status.NOT_FOUND).encoding("Vehicle not found.").build();
                    }else{
                        entity.code = dto.code;
                        entity.designation = dto.designation;
                        entity.vehicle = vehicle;
                        entity.realizedOn = LocalDateTime.parse(dto.realizedOn);
                        entity.expectedOn = LocalDateTime.parse(dto.expectedOn);
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
        Work entity = Work.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            entity.delete();
            return Response.status(Status.OK).entity("Data successfully deleted.").build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Work.listAll()).build();
    }

    private boolean isDTOValid(WorkDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            StringUtils.isNullOrEmpty(dto.designation)||
            dto.vehicleId == null
        ){
            res = false;
        }
        return res;
    }

    private Work isCodeUnique(String code){
        return Work.find("code", code).firstResult();
    }

}