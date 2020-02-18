package com.app.jwt.resources;

import java.time.LocalDate;

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

import com.app.jwt.dto.VehicleInsuranceDTO;
import com.app.jwt.models.VehicleInsurance;
import com.app.jwt.models.Insurance;
import com.app.jwt.models.Vehicle;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/VehicleInsurances")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleInsuranceResource {

    @POST
    @Transactional
    public Response create(VehicleInsuranceDTO dto){
        if(this.isDTOValid(dto)){
            if(this.isCodeUnique(dto.code) == null){
                Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                Insurance insurance = Insurance.findById(dto.insuranceId);
                if(vehicle == null || insurance == null){
                    return Response.status(Status.NOT_FOUND).entity("Impossible to find this VehicleInsurance supplier.").build();
                }else{
                    VehicleInsurance entity = new VehicleInsurance();
                    entity.code = dto.code;
                    entity.vehicle = vehicle;
                    entity.insurance = insurance;
                    entity.startDate = LocalDate.parse(dto.startDate);
                    entity.endDate = LocalDate.parse(dto.endDate);
                    entity.persist();
                    return Response.status(Status.CREATED).entity("VehicleInsurance created successfully.").build();
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
        VehicleInsurance entity = VehicleInsurance.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    public Response update(@PathParam("id") Long id, VehicleInsuranceDTO dto){
        VehicleInsurance entity = VehicleInsurance.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            if(this.isDTOValid(dto)){
                if(this.isCodeUnique(dto.code) == null || this.isCodeUnique(dto.code) == entity){
                    Vehicle vehicle = Vehicle.findById(dto.vehicleId);
                    Insurance insurance = Insurance.findById(dto.insuranceId);
                    if(vehicle == null || insurance == null){
                        return Response.status(Status.NOT_FOUND).entity("Impossible to find this VehicleInsurance supplier.").build();
                    }else{
                        entity.code = dto.code;
                        entity.vehicle = vehicle;
                        entity.insurance = insurance;
                        entity.startDate = LocalDate.parse(dto.startDate);
                        entity.endDate = LocalDate.parse(dto.endDate);
                        entity.persist();
                        return Response.status(Status.CREATED).entity("VehicleInsurance created successfully.").build();
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
        VehicleInsurance entity = VehicleInsurance.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(VehicleInsurance.listAll()).build();
    }

    private boolean isDTOValid(VehicleInsuranceDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            dto.vehicleId == null ||
            dto.insuranceId == null
        ){
            res = false;
        }
        return res;
    }

    private VehicleInsurance isCodeUnique(String code){
        return VehicleInsurance.find("code", code).firstResult();
    }
    
}