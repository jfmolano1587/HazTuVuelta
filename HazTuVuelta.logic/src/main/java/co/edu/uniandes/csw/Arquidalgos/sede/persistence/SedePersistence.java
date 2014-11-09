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
package co.edu.uniandes.csw.Arquidalgos.sede.persistence;

import co.edu.uniandes.csw.Arquidalgos.cita.logic.dto.CitaDTO;
import co.edu.uniandes.csw.Arquidalgos.cita.persistence.api.ICitaPersistence;
import co.edu.uniandes.csw.Arquidalgos.cita.persistence.converter.CitaConverter;
import co.edu.uniandes.csw.Arquidalgos.cita.persistence.entity.CitaEntity;
import co.edu.uniandes.csw.Arquidalgos.sede.logic.dto.SedeDTO;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;

import co.edu.uniandes.csw.Arquidalgos.sede.persistence.api.ISedePersistence;
import co.edu.uniandes.csw.Arquidalgos.turno.logic.dto.TurnoDTO;
import co.edu.uniandes.csw.Arquidalgos.turno.persistence.api.ITurnoPersistence;
import co.edu.uniandes.csw.Arquidalgos.turno.persistence.converter.TurnoConverter;
import co.edu.uniandes.csw.Arquidalgos.usuario.logic.dto.UsuarioDTO;
import co.edu.uniandes.csw.Arquidalgos.usuario.master.logic.dto.UsuarioMasterDTO;
import co.edu.uniandes.csw.Arquidalgos.usuario.master.persistence.UsuarioMasterPersistence;
import co.edu.uniandes.csw.Arquidalgos.usuario.master.persistence.api.IUsuarioMasterPersistence;
import co.edu.uniandes.csw.Arquidalgos.usuario.master.persistence.api._IUsuarioMasterPersistence;
import co.edu.uniandes.csw.Arquidalgos.usuario.master.persistence.entity.UsuariocitasUsEntity;
import co.edu.uniandes.csw.Arquidalgos.usuario.persistence.api.IUsuarioPersistence;
import co.edu.uniandes.csw.Arquidalgos.usuario.persistence.converter.UsuarioConverter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.LocalBean;
import javax.inject.Inject;
import javax.persistence.Query;
import utilidadesHTV.Tiempo;
import utilidadesHTV.ConstantesYMetodos;
import utilidadesHTV.SendEmail;

@Default
@Stateless
@LocalBean
public class SedePersistence extends _SedePersistence implements ISedePersistence {

    @Inject
    private IUsuarioMasterPersistence usuarioMasterPersistance;

    @Inject
    private IUsuarioPersistence usuarioPersistance;

    @Inject
    private ICitaPersistence citaPersistance;

    @Inject
    private ITurnoPersistence turnoPersistence;

    /**
     * Mock, ensayo primer prototipo
     *
     * @return
     */
    public int aumentarTurno() {

        SedeDTO sedeActual = getSede(1L);

        sedeActual.setTurno(sedeActual.getTurno() + 1);

        updateSede(sedeActual);

        return getSede(1L).getTurno();
    }

    /**
     * Mock, ensayo primer prototipo
     *
     * @return
     */
    public int turnoActual() {

        return getSede(1L).getTurno();
    }

    /**
     * Returno todos los turnos de la fila en el dia de hoy en la sede
     *
     * @param idSede
     * @return
     */
    public List<TurnoDTO> darTurnosSedeHoy(Long idSede) {

        List< TurnoDTO> turnosSede = turnoPersistence.getTurnosSede(idSede);
        List< TurnoDTO> resp = new ArrayList<TurnoDTO>();
        Calendar calActual = new GregorianCalendar();
        calActual.setTime(Tiempo.getCurrentDate());

        for (TurnoDTO turno : turnosSede) {

            Calendar calTurno = new GregorianCalendar();
            calTurno.setTime(turno.getFechaTurno());
            if (calActual.get(Calendar.YEAR) == calTurno.get(Calendar.YEAR)
                    && calActual.get(Calendar.MONTH) == calTurno.get(Calendar.MONTH)
                    && calActual.get(Calendar.DAY_OF_MONTH) == calTurno.get(Calendar.DAY_OF_MONTH)) {

                // Son el mismo dia
                resp.add(turno);
            }
        }
        return resp;
    }

    /**
     * Asigna un nuevo turno al final de la fila de hoy en la cede dada, para el
     * usuario dado
     *
     * @param idSede
     * @param correo
     * @return
     * @throws Exception
     */
    //TODO Verificar si hay citas en espera que puedan ser asignadas despu�s de mi
    public int asignarSiguienteTurno(Long idSede, String correo) throws Exception {

        // Verificar que exista usuario
        UsuarioDTO usuario = usuarioPersistance.buscarUsuarioCorreo(correo);

        if (usuario == null) {

            UsuarioDTO nuevoUser = new UsuarioDTO();
            nuevoUser.setCorreo(correo);
            usuario = usuarioPersistance.createUsuario(nuevoUser);
        }

        List<TurnoDTO> turnosHoy = darTurnosSedeHoy(idSede);

        TurnoDTO nuevoTurno = new TurnoDTO();

        nuevoTurno.setFechaTurno(Tiempo.getCurrentDate());

        if (turnosHoy.size() < 1) {
            Date inic = ConstantesYMetodos.darHoraInicioSucursales();
            System.out.println("SedePersistance asignarSiguienteTurno - inic: "+inic.toString());
            nuevoTurno.setHoraInicio(inic);
        } else {
            nuevoTurno.setHoraInicio(turnosHoy.get(turnosHoy.size() - 1).getHoraFinal());
        }

        nuevoTurno.setHoraFinal(new Date(nuevoTurno.getHoraInicio().getTime()
                + (ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS)));
        nuevoTurno.setSedeturnoId(idSede);
        nuevoTurno.setTurno(turnosHoy.size() + 1);

        nuevoTurno = turnoPersistence.createTurno(nuevoTurno);

        TurnoDTO turno2 = turnoPersistence.getTurno(nuevoTurno.getId());
        System.out.println("Sede Persistance asignarSiguienteTurno - turno2: " + turno2.toString());
        System.out.println("Sede Persistance asignarSiguienteTurno - turno2 horas: ID "+ turno2.getId()
                + " Fecha cita: " + turno2.getFechaTurno().toString()
                + " Hora inic: " + turno2.getHoraInicio().toString()
                + " Hora final: " + turno2.getHoraFinal().toString());
        turnosHoy.add(nuevoTurno);

        int turnoAsignado = turnosHoy.size();

        // Relacionar turno y usuario
        List<TurnoDTO> turnoPersistir = new ArrayList<TurnoDTO>();
        turnoPersistir.add(nuevoTurno);
        UsuarioMasterDTO userMaster = new UsuarioMasterDTO();

        userMaster.setId(usuario.getId());
        userMaster.setListturnoUsuario(turnoPersistir);

        // Verificar si hay citas en espera, y a�adirlas a la fila
        if (turnosHoy.size() > 0) {

            List<CitaDTO> citas = citaPersistance.darCitasAnterioresOYa(turnosHoy.get(turnosHoy.size() - 1).getHoraFinal(),idSede);

            for (CitaDTO cita : citas) {
                try {
                    //Falta relacionar cita-turno-usuario
                    cita.setEspera(false);
                    asignarSiguienteTurno(idSede, usuarioMasterPersistance.getUsuarioCitaHoy(cita.getId()).getCorreo());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SedePersistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            enviarEmailNotificaciones(idSede);
        }

        return turnoAsignado;

    }

    /**
     * M�todo que se encarga de terminar el turno que se est� atendiendo, y
     * recalcular los tiempos de los otros turnos en fila
     *
     * @param idSede
     */
    public void atenderTurno(Long idSede) {

        SedeDTO sede = getSede(idSede);
        int turnoActual = sede.getTurno();

        //Correr tiempos por si se demora menos/mas
        List<TurnoDTO> turnos = darTurnosSedeHoy(idSede);
        Date temp = Tiempo.getCurrentDate();
        for (int i = turnoActual; i < turnos.size(); i++) {

            TurnoDTO actual = turnos.get(i);
            actual.setHoraInicio(temp);
            actual.setHoraFinal(new Date(actual.getHoraInicio().getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS));
            temp = actual.getHoraFinal();
        }
        // pasar todas las citas en espera a la fila, si se cumple

        if (turnos.size() > 0) {

            List<CitaDTO> citas = citaPersistance.darCitasAnterioresOYa(turnos.get(turnos.size() - 1).getHoraFinal(),idSede);

            for (CitaDTO cita : citas) {
                try {
                    //Falta relacionar cita-turno-usuario}
                    cita.setEspera(false);
                    asignarSiguienteTurno(idSede, usuarioMasterPersistance.getUsuarioCitaHoy(cita.getId()).getCorreo());

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SedePersistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            sede.setTurno(turnoActual + 1);
            enviarEmailNotificaciones(idSede);
        }

    }

    /**
     * Ultimo turno de la fila
     *
     * @param sede
     * @return
     */
    public int darUltimoTurnoAsignado(Long sede) {

        return darTurnosSedeHoy(sede).size();
    }

    /**
     * Hora en la que inicia el �ltimo turno de la fila
     *
     * @param idSede
     * @return
     */
    public Date darUltimoInicioDeCita(Long idSede) {

        List<TurnoDTO> turnos = darTurnosSedeHoy(idSede);

        if (turnos.size() < 1) {

            return ConstantesYMetodos.darHoraInicioSucursales();
        } else {
            System.out.println("SedePersistance darUltimoInicioDeCita - turnos size "+turnos.size());
            TurnoDTO turno = turnos.get(turnos.size()-1);
            System.out.println("SedePersistance darUltimoInicioDeCita - turno: "+turno.getId()+
                    " fecha "+turno.getFechaTurno()+" hora inic "+turno.getHoraInicio()+" hora fin "+
                    turno.getHoraFinal()+" sede "+turno.getSedeturnoId());
            
            TurnoDTO turno2 = turnoPersistence.getTurno(turno.getId());
            System.out.println("SedePersistance darUltimoInicioDeCita - turno 2: "+turno2.getId()+
                    " fecha "+turno2.getFechaTurno()+" hora inic "+turno2.getHoraInicio()+" hora fin "+
                    turno2.getHoraFinal()+" sede "+turno2.getSedeturnoId());
            Date horaInic = turno2.getHoraInicio();
            System.out.println("SedePersistance darUltimoInicioDeCita - horaInic: "+horaInic.toString());
            return horaInic;
        }
    }

    /**
     * Ultimo turno que han atendido en la sede
     *
     * @param idSede
     * @return
     */
    public int darUltimoTurnoAtendido(Long idSede) {
        System.out.println("sede id :" + idSede);
        int x = getSede(idSede).getTurno();
        System.out.println("Respuesta turno atendido: " + x);
        return x;
    }

    /**
     * Hora aproximada en la se atiende al usuario con la correo dada
     *
     * @param correo
     * @return
     * @throws Exception
     */
    public Date darHoraAproximadaAtencion(String correo) throws Exception {

        CitaDTO cita = usuarioMasterPersistance.getCitaUsuarioHoy(usuarioPersistance.buscarUsuarioCorreo(correo).getId());

        if (cita != null) {

            if (cita.isEspera()) {

                return cita.getHoraIni();

            } else {

                Long inicioUltimoTurno = darUltimoInicioDeCita(cita.getSedecitaId()).getTime();
                int turnoActual = darUltimoTurnoAtendido(cita.getSedecitaId());

                return new Date(inicioUltimoTurno + (cita.getTurnoAsignado() - turnoActual) * ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS);

            }

        } else {

            throw new Exception("El usuario con documento de identificaci�n: " + correo + " no tiene registrado un turno el dia de hoy");

        }
    }

    /**
     * Se le reserva una cita al usuario en el horario dado
     *
     * @param nuevaCita TIENEN QUE PASARLE LA HORA INICIAL DEL RANGO, NO DEL
     * PUESTO DE LA CITA EN EL RANGO
     * @throws Exception
     */
    public void reservarCita(CitaDTO nuevaCita) throws Exception {

        Date date = Tiempo.getCurrentDate();
        System.out.println("Reservar cita SedePersistance: Date " + date.toString());
        Calendar c = new GregorianCalendar();
        System.out.println("Reservar cita SedePersistance: Calendar " + c.toString());
        c.setTime(date);
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                nuevaCita.getHoraInicInt(), 0, 0);

        nuevaCita.setHoraIni(c.getTime());

        System.out.println("Reservar cita SedePersistance: nuevaCita horaini " + nuevaCita.getHoraIni().toString());
        //TODO verificar cuando tiene sentido reservar la cita, y cuando no
        String correoUser = nuevaCita.getName();

        UsuarioDTO user = usuarioPersistance.buscarUsuarioCorreo(correoUser);

        if (user == null) {

            UsuarioDTO nuevoUser = new UsuarioDTO();
            nuevoUser.setCorreo(correoUser);
            user = usuarioPersistance.createUsuario(nuevoUser);

        }
        int cupoCitasHora = ConstantesYMetodos.RANGO_RESERVAR_TURNO_MIN / ConstantesYMetodos.DURACION_APROX_TURNO_MIN;

        System.out.println("Reservar cita SedePersistance: 1 ");
        // Verifica que no se pase el cupo m�ximo de citas que se pueden reservar a esa hora
        if (citaPersistance.darCitasRango(nuevaCita.getHoraIni(),nuevaCita.getSedecitaId()).size() >= cupoCitasHora) {

            throw new Exception("No se pueden reservar m�s turnos a esa hora");
        }
        System.out.println("Reservar cita SedePersistance: 2 ");

        Date horaFinUltimaCita;
        
        if (darTurnosSedeHoy(nuevaCita.getSedecitaId()).size()<1) {
            horaFinUltimaCita = ConstantesYMetodos.darHoraInicioSucursales();
        } else {
            Date horaIniUltimaCita = darUltimoInicioDeCita(nuevaCita.getSedecitaId());
            System.out.println("Reservar cita SedePersistance: horaInicUltimaCita " + horaIniUltimaCita.toString());
            
            Date temp = new Date (horaIniUltimaCita.getTime()+ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS);
            
            System.out.println("Reservar cita SedePersistance: temo " + temp.toString());
            
            horaFinUltimaCita = new Date(temp.getTime());
        }
        System.out.println("Reservar cita SedePersistance: horaFinUltimaCita " + horaFinUltimaCita.toString());
        Calendar cHoraFin = new GregorianCalendar();
        cHoraFin.setTime(horaFinUltimaCita);

        Calendar cFinNuevaCita = new GregorianCalendar();

        cFinNuevaCita.setTime(new Date(nuevaCita.getHoraIni().getTime() + ConstantesYMetodos.RANGO_RESERVAR_TURNO_MILISEGUNDOS));
        System.out.println("Reservar cita SedePersistance: 3 ");
        // Verifica que todavia se pueda reservar una cita en ese rango, es decir, que el ultimo puesto de la fila vaya antes del fin del rango
        if (cHoraFin.after(cFinNuevaCita)) {

            throw new Exception("No se pueden reservar turno a esa hora, reservar en otro rango de horas.");

        }

        Calendar cInicioNuevaCita = new GregorianCalendar();
        cInicioNuevaCita.setTime(nuevaCita.getHoraIni());
        System.out.println("Reservar cita SedePersistance: 4 ");
        // Si ya hay turnos en el rango, en lugar de reservar cita, se pide el turno siguiente
        if (cHoraFin.after(cInicioNuevaCita) || 
                ConstantesYMetodos.citasMismoMinuto(cHoraFin.getTime(), cInicioNuevaCita.getTime())) {
            System.out.println("Reservar cita SedePersistance: 5 ");
            asignarSiguienteTurno(nuevaCita.getSedecitaId(), correoUser);

        } else {
            System.out.println("Reservar cita SedePersistance: 6 ");
            CitaDTO cita = new CitaDTO();
            cita.setEspera(true);
            Date fechaCita = Tiempo.getCurrentDate();
            System.out.println("Reservar cita SedePersistance: Fecha Cita " + fechaCita.toString());
            cita.setFechaCita(fechaCita);

            // Verifica en que posici�n del rango se piensa asignar la cita
            List<CitaDTO> citasRango = citaPersistance.darCitasRango(nuevaCita.getHoraIni(),nuevaCita.getSedecitaId());

            if (citasRango.size() < 1) {

                cita.setHoraIni(nuevaCita.getHoraIni());
            } else {

                cita.setHoraIni(citasRango.get(citasRango.size() - 1).getHoraFin());
            }
            System.out.println("Reservar cita SedePersistance: 7 ");
            cita.setHoraFin(new Date(cita.getHoraIni().getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS));
            cita.setSedecitaId(nuevaCita.getSedecitaId());
            cita.setTurnoAsignado(-1);

            System.out.println("Reservar cita SedePersistance: Hora inicio " + cita.getHoraIni().toString());
            System.out.println("Reservar cita SedePersistance: Hora final " + cita.getHoraFin().toString());
            System.out.println("Reservar cita SedePersistance: Cita " + cita.toString());
            cita = citaPersistance.createCita(cita);

            // Relacionar cita y usuario
            List<CitaDTO> citaPersistir = new ArrayList<CitaDTO>();
            citaPersistir.add(cita);
            UsuarioMasterDTO userMaster = new UsuarioMasterDTO();

            UsuariocitasUsEntity usCitas = new UsuariocitasUsEntity();
            usCitas.setCitasUsId(cita.getId());
            usCitas.setCitasUsIdEntity(CitaConverter.persistenceDTO2Entity(cita));
            usCitas.setUsuarioId(user.getId());

            usuarioMasterPersistance.createUsuariocitasUsEntity(usCitas);

        }
    }

    public int darTurnosNoAtendidosSede(Long idSede) {

        SedeDTO sede = getSede(idSede);

        return darTurnosSedeHoy(idSede).size() - sede.getTurno();
    }

    private void enviarEmailNotificaciones(Long idSede) {
        
        // Turnos no atendidos
        
        List<TurnoDTO> turnosHoy = darTurnosSedeHoy(idSede);
        SedeDTO sede = getSede(idSede);
        Calendar c = new GregorianCalendar();
        c.setTime(Tiempo.getCurrentDate());
        
        for (TurnoDTO turnoHoy : turnosHoy) {
            
            Calendar cTurno = new GregorianCalendar();
            cTurno.setTime(turnoHoy.getHoraInicio());
            if ( cTurno.after(c)){
                
                long minutos = (cTurno.getTimeInMillis()-c.getTimeInMillis())*60000;
                
                if ( minutos < 30){
                    
                    UsuarioDTO user = usuarioMasterPersistance.getUsuarioTurnoHoy(turnoHoy.getId());
                    SendEmail sendEmail = new SendEmail(user.getCorreo(), minutos+" minutos", turnoHoy.getTurno()-sede.getTurno());
                    sendEmail.start();
                }
                
            }
            
        }
        
    
        // Citas Espera
        
        List<CitaDTO> citasHoy = citaPersistance.darCitasHoy(idSede);
        
        for (CitaDTO citaHoy : citasHoy) {
            
            Calendar cTurno = new GregorianCalendar();
            cTurno.setTime(citaHoy.getHoraIni());
            if ( citaHoy.isEspera() && cTurno.after(c)){
                
                long minutos = (cTurno.getTimeInMillis()-c.getTimeInMillis())*60000;
                
                if ( minutos < 30){
                    
                    UsuarioDTO user = usuarioMasterPersistance.getUsuarioCitaHoy(citaHoy.getId());
                    SendEmail sendEmail = new SendEmail(user.getCorreo(), minutos+" minutos", -1);
                    sendEmail.start();
                }
                
            }
            
        }
        
    }

}
