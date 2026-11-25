package cl.duoc.amigosecreto.dao;

import cl.duoc.amigosecreto.model.Adulto;
import cl.duoc.amigosecreto.model.Nino;
import cl.duoc.amigosecreto.model.Participante;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ParticipanteMapper {

    public static Participante mapear(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        int edad = rs.getInt("edad");
        String sugerenciaRegalo = rs.getString("sugerencia_regalo");
        int idAmigoSecreto = rs.getInt("id_amigo_secreto");
        Participante participante;
        int tipo = rs.getInt("id_tipo");
        if(tipo == 1){
            // subtipo Adulto
            boolean trabajadorActivo = rs.getBoolean("trabajador_activo");
            participante = new Adulto(nombre,edad,sugerenciaRegalo,trabajadorActivo);
        }else{
            // subtipo Nino
            String nombreTutor = rs.getString("nombre_tutor");
            participante = new Nino(nombre,edad,sugerenciaRegalo,nombreTutor);
        }
        participante.setId(id);
        participante.setIdAmigoSecreto(idAmigoSecreto);

        return participante;
    }
}
