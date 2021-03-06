/* ========================================================================
 * Copyright 2014 Arquidalgos
 *
 * Licensed under the MIT, The MIT License (MIT)
 * Copyright (c) 2014 Arquidalgos

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 * ========================================================================


Source generated by CrudMaker version 1.0.0.201408112050

*/

package co.edu.uniandes.csw.Arquidalgos.sede.service;

import co.edu.uniandes.csw.Arquidalgos.cita.logic.dto.CitaDTO;
import co.edu.uniandes.csw.Arquidalgos.sede.logic.dto.SedeDTO;
import co.edu.uniandes.csw.Arquidalgos.turno.logic.dto.TurnoDTO;
import co.edu.uniandes.csw.Arquidalgos.usuario.logic.dto.UsuarioDTO;
import java.util.List;

import javax.ejb.Stateless;
import javax.json.Json;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/Sede")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SedeService extends _SedeService {

    @POST
    @Path("/aumentarTurno")
    public String aumentarTurno() throws Exception {
        return this.sedeLogicService.aumentarTurno();
    }
    
    
    @POST
    @Path("/turnoActual")
    public String turnoActual() throws Exception {
        return this.sedeLogicService.turnoActual();
    }
    
    @GET
    @Path("/turnoAtendido/{id}")
    public Integer turnoAtendido(@PathParam("id") Long id) throws Exception {
        Long idSede = new Long(id);
        System.out.println("Service, turno atendido: "+idSede);
        return this.sedeLogicService.darUltimoTurnoAtendido(idSede);
    }
    
    @GET
    @Path("/horaAtencion/{id}")
    public String darHoraDeAtencion(@PathParam("id") String correo) throws Exception {
        System.out.println("Service, hora de atención: "+correo);
        return this.sedeLogicService.darHoraAproximadaAtencion(correo);
    }
    
    @GET
    @Path("/turnos/{id}")
    public List<TurnoDTO> turnosSede(@PathParam("id") Long id) throws Exception {
        Long idSede = new Long(id);
        System.out.println("Service, turnos de la sede: "+idSede);
        return this.sedeLogicService.darTurnosNoAtendidosSede(idSede);
    }
    
    @POST
    @Path("/reservar")
    public String turnoActual(CitaDTO cita) throws Exception {
        
        return this.sedeLogicService.reservarCita(cita);
    }
    
    @GET
    @Path("/siguienteTurno/{id}")
    public String siguienteTurno(@PathParam("id") Long id) throws Exception {
        Long idSede = new Long(id);
        System.out.println("Service, siguiente turno de sede: "+idSede);
        
        return  "{'data':" + this.sedeLogicService.atenderTurno(idSede)+ "}";
    }   
}