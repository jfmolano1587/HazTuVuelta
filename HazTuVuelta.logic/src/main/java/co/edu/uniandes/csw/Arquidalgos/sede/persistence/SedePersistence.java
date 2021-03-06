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
import co.edu.uniandes.csw.Arquidalgos.usuario.master.persistence.entity.UsuarioturnoUsuarioEntity;
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
    private ITurnoPersistence turnoPersistance;

    /**
     * Mock, ensayo primer prototipo
     *
     * @return
     */
    public Integer aumentarTurno() {

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
    public Integer turnoActual() {

        return getSede(1L).getTurno();
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
    public Integer asignarSiguienteTurno(Long idSede, String correo) throws Exception {

        // Verificar que exista usuario
        UsuarioDTO usuario = usuarioPersistance.buscarUsuarioCorreo(correo);

        if (usuario == null) {

            UsuarioDTO nuevoUser = new UsuarioDTO();
            nuevoUser.setCorreo(correo);
            usuario = usuarioPersistance.createUsuario(nuevoUser);
        }

        List<TurnoDTO> turnosHoy = turnoPersistance.darTurnosSedeHoy(idSede);

        TurnoDTO nuevoTurno = new TurnoDTO();

        nuevoTurno.setFechaTurno(Tiempo.getCurrentDate());
        nuevoTurno.setName(correo);
        if (turnosHoy.size() < 1) {
            Date inic = ConstantesYMetodos.darHoraInicioSucursales();
            System.out.println("SedePersistance asignarSiguienteTurno - inic: " + inic.toString());
            nuevoTurno.setHoraInicio(inic);
        } else {
            nuevoTurno.setHoraInicio(turnosHoy.get(turnosHoy.size() - 1).getHoraFinal());
        }

        nuevoTurno.setHoraFinal(new Date(nuevoTurno.getHoraInicio().getTime()
                + (ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS)));
        nuevoTurno.setSedeturnoId(idSede);
        nuevoTurno.setTurno(turnosHoy.size() + 1);

        nuevoTurno = turnoPersistance.createTurno(nuevoTurno);

        turnosHoy.add(nuevoTurno);

        int turnoAsignado = turnosHoy.size();

        // Relacionar turno y usuario
        UsuarioturnoUsuarioEntity userTurnoEntity = new UsuarioturnoUsuarioEntity();

        userTurnoEntity.setTurnoUsuarioId(nuevoTurno.getId());
        userTurnoEntity.setUsuarioId(usuario.getId());
        userTurnoEntity.setTurnoUsuarioIdEntity(TurnoConverter.persistenceDTO2Entity(nuevoTurno));

        usuarioMasterPersistance.createUsuarioturnoUsuarioEntity(userTurnoEntity);
        // Verificar si hay citas en espera, y a�adirlas a la fila
        if (turnosHoy.size() > 0) {

            List<CitaDTO> citas = citaPersistance.darCitasAnterioresOYa(turnosHoy.get(turnosHoy.size() - 1).getHoraFinal(), idSede);

            for (CitaDTO cita : citas) {
                try {
                    //Falta relacionar cita-turno-usuario
                    cita.setEspera(false);
                    int turno = asignarSiguienteTurno(idSede, usuarioMasterPersistance.getUsuarioCitaHoy(cita.getId()).getCorreo());
                    cita.setTurnoAsignado(turno);
                    citaPersistance.updateCita(cita);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SedePersistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            enviarEmailNotificaciones(idSede);
        }

        enviarEmailNotificacionInicial(correo, nuevoTurno.getHoraInicio(), ConstantesYMetodos.TIPO_TURNO, getSede(idSede).getTurno(), turnoAsignado);
        return turnoAsignado;

    }

    /**
     * M�todo que se encarga de terminar el turno que se est� atendiendo, y
     * recalcular los tiempos de los otros turnos en fila
     *
     * @param idSede
     * @return
     */
    public Integer atenderTurno(Long idSede) {

        SedeDTO sede = getSede(idSede);
        int turnoActual = sede.getTurno();

        //Correr tiempos por si se demora menos/mas
        List<TurnoDTO> turnos = turnoPersistance.darTurnosSedeHoy(idSede);
        Date temp = Tiempo.getCurrentDate();
        for (int i = turnoActual; i < turnos.size(); i++) {

            TurnoDTO actual = turnos.get(i);
            actual.setHoraInicio(temp);
            actual.setHoraFinal(new Date(actual.getHoraInicio().getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS));
            temp = actual.getHoraFinal();
            turnoPersistance.updateTurno(actual);
        }
        // pasar todas las citas en espera a la fila, si se cumple

        if (turnos.size() > 0) {

            List<CitaDTO> citas = citaPersistance.darCitasAnterioresOYa(turnos.get(turnos.size() - 1).getHoraFinal(), idSede);

            for (CitaDTO cita : citas) {
                try {
                    //Falta relacionar cita-turno-usuario}
                    cita.setEspera(false);
                    int turno = asignarSiguienteTurno(idSede, usuarioMasterPersistance.getUsuarioCitaHoy(cita.getId()).getCorreo());
                    cita.setTurnoAsignado(turno);
                    citaPersistance.updateCita(cita);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Logger.getLogger(SedePersistence.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            sede.setTurno(turnoActual + 1);
            updateSede(sede);
            enviarEmailNotificaciones(idSede);
        }

        return sede.getTurno();
    }

    /**
     * Ultimo turno de la fila
     *
     * @param sede
     * @return
     */
    public Integer darUltimoTurnoAsignado(Long sede) {

        return turnoPersistance.darTurnosSedeHoy(sede).size();
    }

    /**
     * Hora en la que inicia el �ltimo turno de la fila
     *
     * @param idSede
     * @return
     */
    public Date darUltimoInicioDeTurno(Long idSede) {

        List<TurnoDTO> turnos = turnoPersistance.darTurnosSedeHoy(idSede);

        if (turnos.size() < 1) {

            return ConstantesYMetodos.darHoraInicioSucursales();
        } else {
            System.out.println("SedePersistance darUltimoInicioDeCita - turnos size " + turnos.size());
            TurnoDTO turno = turnos.get(turnos.size() - 1);
            System.out.println("SedePersistance darUltimoInicioDeCita - turno: " + turno.getId()
                    + " fecha " + turno.getFechaTurno() + " hora inic " + turno.getHoraInicio() + " hora fin "
                    + turno.getHoraFinal() + " sede " + turno.getSedeturnoId());

            TurnoDTO turno2 = turnoPersistance.getTurno(turno.getId());
            System.out.println("SedePersistance darUltimoInicioDeCita - turno 2: " + turno2.getId()
                    + " fecha " + turno2.getFechaTurno() + " hora inic " + turno2.getHoraInicio() + " hora fin "
                    + turno2.getHoraFinal() + " sede " + turno2.getSedeturnoId());
            Date horaInic = turno2.getHoraInicio();
            System.out.println("SedePersistance darUltimoInicioDeCita - horaInic: " + horaInic.toString());
            return horaInic;
        }
    }

    /**
     * Ultimo turno que han atendido en la sede
     *
     * @param idSede
     * @return
     */
    public Integer darUltimoTurnoAtendido(Long idSede) {
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

        UsuarioDTO user = usuarioPersistance.buscarUsuarioCorreo(correo);

        if (user == null) {

            throw new Exception("El usuario no tiene registrado un turno el dia de hoy");

        }

        CitaDTO cita = usuarioMasterPersistance.getCitaUsuarioHoy(user.getId());

        if (cita != null) {

            System.out.println("Sede Persistance darHoraAprox - cita: ID " + cita.getId() + " name "
                    + cita.getName() + " fecha " + cita.getFechaCita() + " horaIni " + cita.getHoraIni()
                    + " horaFin " + cita.getHoraFin() + " sede " + cita.getSedecitaId() + " numeroTurno "
                    + cita.getTurnoAsignado() + " espera " + cita.isEspera());

            if (cita.isEspera()) {

                return cita.getHoraIni();

            } else {

                TurnoDTO turno = turnoPersistance.darTurnoPorNumeroTurno(cita.getTurnoAsignado(), cita.getSedecitaId());

                return turno.getHoraInicio();
            }

        } else {

            TurnoDTO turno = usuarioMasterPersistance.getTurnoUsuarioHoy(user.getId());

            if (turno == null) {

                throw new Exception("El usuario no tiene registrado un turno el dia de hoy");
            }

            return turno.getHoraInicio();

        }
    }

    /**
     * Se le reserva una cita al usuario en el horario dado
     *
     * @param nuevaCita TIENEN QUE PASARLE LA HORA INICIAL DEL RANGO, NO DEL
     * PUESTO DE LA CITA EN EL RANGO
     * @return 
     * @throws Exception
     */
    public String reservarCita(CitaDTO nuevaCita) throws Exception {

        if (nuevaCita.getHoraInicInt() == -1) {

            int resp = asignarSiguienteTurno(nuevaCita.getSedecitaId(), nuevaCita.getName());
            
            return resp+"";
        } else {
            Date date = Tiempo.getCurrentDate();
            System.out.println("Reservar cita SedePersistance: Date " + date.toString());
            Calendar c = new GregorianCalendar();
            System.out.println("Reservar cita SedePersistance: Calendar " + c.toString());
            c.setTime(date);
            c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH),
                    nuevaCita.getHoraInicInt(), 0, 0);
            c.set(Calendar.MILLISECOND, 0);
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
            if (citaPersistance.darCitasRango(nuevaCita.getHoraIni(), nuevaCita.getSedecitaId()).size() >= cupoCitasHora) {

                throw new Exception("No se pueden reservar m�s turnos a esa hora");
            }
            System.out.println("Reservar cita SedePersistance: 2 ");

            Date horaFinUltimaCita;

            if (turnoPersistance.darTurnosSedeHoy(nuevaCita.getSedecitaId()).size() < 1) {
                horaFinUltimaCita = ConstantesYMetodos.darHoraInicioSucursales();
            } else {
                Date horaIniUltimaCita = darUltimoInicioDeTurno(nuevaCita.getSedecitaId());
                System.out.println("Reservar cita SedePersistance: horaInicUltimaCita " + horaIniUltimaCita.toString());

                Date temp = new Date(horaIniUltimaCita.getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS);

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
            if (cHoraFin.after(cInicioNuevaCita)
                    || ConstantesYMetodos.citasMismoMinuto(cHoraFin.getTime(), cInicioNuevaCita.getTime())) {
                System.out.println("Reservar cita SedePersistance: 5 ");
                int resp = asignarSiguienteTurno(nuevaCita.getSedecitaId(), correoUser);

                return resp+"";
            } 
            else {
                System.out.println("Reservar cita SedePersistance: 6 ");
                CitaDTO cita = new CitaDTO();
                cita.setEspera(true);
                Date fechaCita = Tiempo.getCurrentDate();
                System.out.println("Reservar cita SedePersistance: Fecha Cita " + fechaCita.toString());
                cita.setFechaCita(fechaCita);

                // Verifica en que posici�n del rango se piensa asignar la cita
                List<CitaDTO> citasRango = citaPersistance.darCitasRango(nuevaCita.getHoraIni(), nuevaCita.getSedecitaId());

                if (citasRango.size() < 1) {

                    System.out.println("Reservar cita SedePersistance: citasRango < 1 ");
                    cita.setHoraIni(nuevaCita.getHoraIni());
                } else {

                    System.out.println("Reservar cita SedePersistance: citasRango >= 1 ");
                    cita.setHoraIni(citasRango.get(citasRango.size() - 1).getHoraFin());
                }
                System.out.println("Reservar cita SedePersistance: 7 ");
                cita.setName(correoUser);
                cita.setHoraFin(new Date(cita.getHoraIni().getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS));
                cita.setSedecitaId(nuevaCita.getSedecitaId());
                cita.setTurnoAsignado(-1);

                System.out.println("Reservar cita SedePersistance: Hora inicio " + cita.getHoraIni().toString());
                System.out.println("Reservar cita SedePersistance: Hora final " + cita.getHoraFin().toString());

                System.out.println("Reservar cita SedePersistance: - cita: ID " + cita.getId() + " name "
                        + cita.getName() + " fecha " + cita.getFechaCita() + " horaIni " + cita.getHoraIni()
                        + " horaFin " + cita.getHoraFin() + " sede " + cita.getSedecitaId() + " numeroTurno "
                        + cita.getTurnoAsignado() + " espera " + cita.isEspera());

                cita = citaPersistance.createCita(cita);

                // Relacionar cita y usuario
                UsuariocitasUsEntity usCitas = new UsuariocitasUsEntity();
                usCitas.setCitasUsId(cita.getId());
                usCitas.setCitasUsIdEntity(CitaConverter.persistenceDTO2Entity(cita));
                usCitas.setUsuarioId(user.getId());

                usuarioMasterPersistance.createUsuariocitasUsEntity(usCitas);

                enviarEmailNotificacionInicial(correoUser, cita.getHoraIni(), ConstantesYMetodos.TIPO_CITA_ESPERA, 0, 0);
                
                return "-1";
            }
        }
    }

    public Integer darNumeroTurnosNoAtendidosSede(Long idSede) {

        SedeDTO sede = getSede(idSede);

        return turnoPersistance.darTurnosSedeHoy(idSede).size() - sede.getTurno();
    }

    private void enviarEmailNotificaciones(Long idSede) {

        System.out.println("SedePersistance enviarEmail - idSede: " + idSede);

        // Turnos no atendidos
        List<TurnoDTO> turnosHoy = turnoPersistance.darTurnosSedeHoy(idSede);
        SedeDTO sede = getSede(idSede);
        Calendar c = new GregorianCalendar();
        c.setTime(Tiempo.getCurrentDate());

        for (TurnoDTO turnoHoy : turnosHoy) {

            Calendar cTurno = new GregorianCalendar();
            cTurno.setTime(turnoHoy.getHoraInicio());
            System.out.println("SedePersistance enviarEmail - calendarios: " + idSede);
            if (cTurno.after(c)) {

                long minutos = (cTurno.getTimeInMillis() - c.getTimeInMillis()) * 60000;

                if (minutos < 30) {

                    UsuarioDTO user = usuarioMasterPersistance.getUsuarioTurnoHoy(turnoHoy.getId());
                    SendEmail sendEmail = new SendEmail(user.getCorreo(), minutos + " minutos", turnoHoy.getTurno() - sede.getTurno());
                    sendEmail.start();
                }

            }

        }

        // Citas Espera
        List<CitaDTO> citasHoy = citaPersistance.darCitasHoy(idSede);

        for (CitaDTO citaHoy : citasHoy) {

            Calendar cTurno = new GregorianCalendar();
            cTurno.setTime(citaHoy.getHoraIni());
            if (citaHoy.isEspera() && cTurno.after(c)) {

                long minutos = (cTurno.getTimeInMillis() - c.getTimeInMillis()) * 60000;

                if (minutos < 30) {

                    UsuarioDTO user = usuarioMasterPersistance.getUsuarioCitaHoy(citaHoy.getId());
                    SendEmail sendEmail = new SendEmail(user.getCorreo(), minutos + " minutos", -1);
                    sendEmail.start();
                }

            }

        }

    }

    private void enviarEmailNotificacionInicial(String correo, Date horaInicial, int tipoTurno, int turnoActual, int turnoUsuario) {

        System.out.println("SedePersistance enviarEmailInicial - correo: " + correo);
        String mensaje;
        Calendar c = new GregorianCalendar();
        c.setTime(horaInicial);
        String hora = c.get(Calendar.HOUR) + "";
        hora = (hora.length() == 1) ? "0" + hora : hora;
        String minutos = c.get(Calendar.MINUTE) + "";
        minutos = (minutos.length() == 1) ? "0" + minutos : minutos;
        String amPm = (c.get(Calendar.AM_PM) == Calendar.AM) ? "am" : "pm";
        if (tipoTurno == ConstantesYMetodos.TIPO_CITA_ESPERA) {

            mensaje = "Usted acabo de reservar un turno en Haz Tu Vuelta. \nSe estima que te atenderan"
                    + " a esta hora: " + hora + ":" + minutos + amPm + ". \n"
                    + "Si te gusta nuestra aplicacion envianos un mensaje a arquidalgos@hotmail.com";
            SendEmail sendEmail = new SendEmail(correo, mensaje, -2);
            sendEmail.start();

        } else if (tipoTurno == ConstantesYMetodos.TIPO_TURNO) {

            mensaje = "Usted acabo de reservar un turno en Haz Tu Vuelta. \nSe estima que te atenderan"
                    + " a esta hora: " + hora + ":" + minutos + amPm + ". \nActualmente se esta atendiendo el turno "
                    + turnoActual + " y tu turno es el " + turnoUsuario + ". \n"
                    + "Si te gusta nuestra aplicacion envianos un mensaje a arquidalgos@hotmail.com";
            SendEmail sendEmail = new SendEmail(correo, mensaje, -2);
            sendEmail.start();
        }
    }

    /**
     * Verifica si un usuario ya pidi� un turno o una cita hoy en alguna
     * sucursal
     *
     * @param correo
     * @return
     */
    public boolean yaReservoCitaOTurnoHoy(String correo) {

        List<SedeDTO> sedes = getSedes();

        for (SedeDTO sede : sedes) {

            List<TurnoDTO> turnos = turnoPersistance.darTurnosSedeHoy(sede.getId());
            List<CitaDTO> citas = citaPersistance.darCitasHoy(sede.getId());

            for (TurnoDTO turno : turnos) {

                String correoTurno = usuarioMasterPersistance.getUsuarioTurnoHoy(turno.getId()).getCorreo();
                if (correoTurno.equals(correo)) {
                    return true;
                }
            }

            for (CitaDTO cita : citas) {
                String correoCita = usuarioMasterPersistance.getUsuarioCitaHoy(cita.getId()).getCorreo();

                if (correoCita.equals(correo)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void cancelarTurnoOCita(String correo) {

        UsuarioDTO user = usuarioPersistance.buscarUsuarioCorreo(correo);
        if (user != null) {

            TurnoDTO turno = usuarioMasterPersistance.getTurnoUsuarioHoy(user.getId());

            if (turno != null) {

                List<TurnoDTO> turnosHoy = turnoPersistance.darTurnosSedeHoy(turno.getSedeturnoId());
                Date temp = turno.getHoraInicio();
                for (int i = turno.getTurno() - 1; i < turnosHoy.size(); i++) {
                    TurnoDTO actual = turnosHoy.get(i);
                    actual.setHoraInicio(temp);
                    actual.setHoraFinal(new Date(actual.getHoraInicio().getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS));
                    temp = actual.getHoraFinal();
                    turnoPersistance.updateTurno(actual);
                }

            } else {

                CitaDTO cita = usuarioMasterPersistance.getCitaUsuarioHoy(user.getId());

                if (cita != null && cita.isEspera()) {

                    Calendar c = new GregorianCalendar();
                    c.setTime(cita.getHoraIni());
                    c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), 0);

                    List<CitaDTO> citasRango = citaPersistance.darCitasRango(c.getTime(), cita.getSedecitaId());
                    int posicionCita = posicionCita(correo);

                    Date temp = cita.getHoraIni();
                    for (int i = posicionCita; i < 10; i++) {
                        CitaDTO actual = citasRango.get(i);
                        actual.setHoraIni(temp);
                        actual.setHoraFin(new Date(actual.getHoraIni().getTime() + ConstantesYMetodos.DURACION_APROX_TURNO_MILISEGUNDOS));
                        temp = actual.getHoraFin();
                        citaPersistance.updateCita(actual);

                    }

                }
            }
        }
    }

    /**
     * Retorna la posicion de la cita dentro del rango de citas que hay ( 1-12)
     *
     * @param correo
     * @return
     */
    public Integer posicionCita(String correo) {

        UsuarioDTO user = usuarioPersistance.buscarUsuarioCorreo(correo);
        if (user == null) {
            return -1;
        }

        CitaDTO cita = usuarioMasterPersistance.getCitaUsuarioHoy(user.getId());

        if (!cita.isEspera()) {
            return 0;
        }

        Calendar cCita = new GregorianCalendar();
        cCita.setTime(cita.getHoraIni());
        return ConstantesYMetodos.RANGO_RESERVAR_TURNO_MIN / cCita.get(Calendar.MINUTE);
    }

    public List<TurnoDTO> darTurnosNoAtendidosSede(Long idSede) {

        SedeDTO sede = getSede(idSede);

        List<TurnoDTO> resp = new ArrayList<TurnoDTO>();
        List<TurnoDTO> turnosSede = turnoPersistance.darTurnosSedeHoy(idSede);

        for (int i = sede.getTurno(); i < turnosSede.size(); i++) {

            resp.add(turnosSede.get(i));
        }

        return resp;
    }
}
