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

import com.app.jwt.dto.VehicleDTO;
import com.app.jwt.models.Model;
import com.app.jwt.models.User;
import com.app.jwt.models.Vehicle;
import com.mysql.cj.util.StringUtils;

@Path("/api/v1/Vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class VehicleResource {

    @POST
    @Transactional
    public Response create(VehicleDTO dto){
        if(this.isDTOValid(dto)){
            if(this.isCodeUnique(dto.code) == null){
                Model model = Model.findById(dto.modelId);
                User assignedTo = User.findById(dto.assignedToId);
                User drivedBy = User.findById(dto.drivedById);
                if(model == null){
                    return Response.status(Status.NOT_FOUND).encoding("Impossible to find the model of this vehicle.").build();
                }else{
                    Vehicle entity = new Vehicle();
                    entity.code = dto.code;
                    entity.designation = dto.designation;
                    entity.description = dto.description;
                    entity.builtOn = dto.builtOn;
                    entity.purchasedOn = LocalDate.parse(dto.purchasedOn);
                    entity.model = model;
                    entity.assignedTo = assignedTo;
                    entity.drivedBy = drivedBy;
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
        Vehicle entity = Vehicle.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            return Response.status(Status.OK).entity(entity).build();
        }
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") Long id, VehicleDTO dto){
        Vehicle entity = Vehicle.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).build();
        }else{
            if(this.isDTOValid(dto)){
                if(this.isCodeUnique(dto.code) == null){
                    Model model = Model.findById(dto.modelId);
                    User assignedTo = User.findById(dto.assignedToId);
                    User drivedBy = User.findById(dto.drivedById);
                    if(model == null){
                        return Response.status(Status.NOT_FOUND).encoding("Impossible to find the model of this vehicle.").build();
                    }else{
                        entity.code = dto.code;
                        entity.designation = dto.designation;
                        entity.description = dto.description;
                        entity.builtOn = dto.builtOn;
                        entity.purchasedOn = LocalDate.parse(dto.purchasedOn);
                        entity.model = model;
                        entity.assignedTo = assignedTo;
                        entity.drivedBy = drivedBy;
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
        Vehicle entity = Vehicle.findById(id);
        if(entity == null){
            return Response.status(Status.NOT_FOUND).entity("Impossible to find this data.").build();
        }else{
            entity.delete();
            return Response.status(Status.OK).entity("Data successfully deleted.").build();
        }
    }

    @GET
    public Response list(){
        return Response.status(Status.OK).entity(Vehicle.listAll()).build();
    }

    private boolean isDTOValid(VehicleDTO dto){
        boolean res = true;
        if(
            StringUtils.isNullOrEmpty(dto.code)||
            dto.modelId == null
        ){
            res = false;
        }
        return res;
    }

    private Vehicle isCodeUnique(String code){
        return Vehicle.find("code", code).firstResult();
    }

}