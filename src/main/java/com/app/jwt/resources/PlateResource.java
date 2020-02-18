package com.app.jwt.resources;

import java.time.LocalDate;

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

import com.app.jwt.dto.PlateDTO;
import com.app.jwt.models.Plate;
import com.app.jwt.models.Vehicle;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/plates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PlateResource {

    @POST
    public Response create(PlateDTO dto){
        if(!this.isValidDTO(dto)){
            return Response.status(Status.BAD_REQUEST).entity("Invalid credentials to create a plate.").build();
        }else{
            if(this.isCodeUnique(dto.value) == null){
                Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                if(vehicle == null){
                    return Response.status(Status.BAD_REQUEST).entity("Impossible to find the vehicle.").build();
                }else{
                    Plate entity = new Plate();
                    entity.expirationDate = (!StringUtils.isNullOrEmpty(dto.expirationDate)) ? LocalDate.parse(dto.expirationDate) : null;
                    entity.isCurrent = dto.isCurrent;
                    entity.value = dto.value;
                    entity.persist();
                    return Response.status(Status.CREATED).entity(entity).build();
                }
            }else{
                return Response.status(Status.BAD_REQUEST).entity("This plate value is already used.").build();
            }
        }
    }

    @GET
    @Path("{id}")
    public Response read(@PathParam("id") Long id){
        Plate entity = Plate.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this plate.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, PlateDTO dto){
        Plate entity = Plate.findById(id);
        if(entity == null){
            return Response.status(Status.BAD_REQUEST).entity("Impossible to find the plate.").build();
        }else{
            if(!this.isValidDTO(dto)){
                return Response.status(Status.BAD_REQUEST).entity("Invalid credentials to save this plate.").build();
            }else{
                if(this.isCodeUnique(dto.value) == null){
                    Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                    if(vehicle == null){
                        return Response.status(Status.BAD_REQUEST).entity("Impossible to find the vehicle.").build();
                    }else{
                        entity.expirationDate = (!StringUtils.isNullOrEmpty(dto.expirationDate)) ? LocalDate.parse(dto.expirationDate) : null;
                        entity.isCurrent = dto.isCurrent;
                        entity.value = dto.value;
                        entity.persist();
                        return Response.status(Status.CREATED).entity(entity).build();
                    }
                }else{
                    return Response.status(Status.BAD_REQUEST).entity("This plate value is already used.").build();
                }
            }
        }
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id){
        Plate entity = Plate.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this plate.").build();
        }else{
            entity.delete();
            return Response.status(Status.OK).entity("Plate successfully deleted.").build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Plate.listAll()).build();
    }

    private Plate isCodeUnique(String plateValue){
        return Plate.find("value", plateValue).firstResult();
    }

    private boolean isValidDTO(PlateDTO dto){
        return !StringUtils.isNullOrEmpty(dto.value);
    }
    
}