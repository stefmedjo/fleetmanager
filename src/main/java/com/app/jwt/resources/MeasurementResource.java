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

import com.app.jwt.dto.MeasurementDTO;
import com.app.jwt.models.Measurement;
import com.app.jwt.models.Vehicle;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/Measurements")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MeasurementResource {

    @POST
    @Transactional
    public Response create(MeasurementDTO dto){
        if(this.isDTOValid(dto)){
            Vehicle vehicle = Vehicle.findById(dto.vehicleId);
            if(vehicle == null){
                return Response.status(Status.NOT_FOUND).entity("Impossible to find this measurement vehicle.").build();
            }else{
                if(this.isCodeUnique(dto.code) == null){
                    Measurement entity = new Measurement();
                    entity.code = dto.code;
                    entity.fuelConsumption = dto.fuelConsumption;
                    entity.vehicle = vehicle;
                    entity.persist();
                    return Response.status(Status.CREATED).entity("Item created successfully").build();
                }else{
                    return Response.status(Status.BAD_REQUEST).entity("Code already used").build();
                }
            }
        }else{
            return Response.status(Status.BAD_REQUEST).entity("Invalid Credentials.").build();
        }
    }

    @GET
    public Response read(@PathParam("id") Long id){
        Measurement entity = Measurement.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    public Response update(@PathParam("id") Long id, MeasurementDTO dto){
        Measurement entity = Measurement.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            if(this.isDTOValid(dto)){
                Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                if(vehicle == null){
                    return Response.status(Status.NOT_FOUND).entity("Impossible to find this measurement vehicle.").build();
                }else{
                    if(this.isCodeUnique(dto.code) == null){
                        entity.code = dto.code;
                        entity.fuelConsumption = dto.fuelConsumption;
                        entity.vehicle = vehicle;
                        entity.persist();
                        return Response.status(Status.CREATED).entity("Item created successfully").build();
                    }else{
                        return Response.status(Status.BAD_REQUEST).entity("Code already used").build();
                    }
                }
            }else{
                return Response.status(Status.BAD_REQUEST).entity("Invalid Credentials.").build();
            }
        }
    }

    @DELETE
    public Response delete(@PathParam("id") Long id){
        Measurement entity = Measurement.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Measurement.listAll()).build();
    }

    private boolean isDTOValid(MeasurementDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            dto.vehicleId == null
        ){
            res = false;
        }
        return res;
    }

    private Measurement isCodeUnique(String code){
        return Measurement.find("code", code).firstResult();
    }
    
}