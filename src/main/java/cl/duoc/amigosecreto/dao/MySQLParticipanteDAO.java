package cl.duoc.amigosecreto.dao;

import cl.duoc.amigosecreto.conexion.ConexionBD;
import cl.duoc.amigosecreto.model.Adulto;
import cl.duoc.amigosecreto.model.Nino;
import cl.duoc.amigosecreto.model.Participante;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class MySQLParticipanteDAO implements ParticipanteDAO {
    private static final String SELECT_ALL = "SELECT * FROM participante";
    private static final String INSERT_ADULTO = "INSERT INTO participante (id_tipo, nombre, edad, sugerencia_regalo,trabajador_activo) VALUES (1,?,?,?,?)";
    private static final String INSERT_NINO = "INSERT INTO participante (id_tipo, nombre, edad, sugerencia_regalo,nombre_tutor) VALUES (2,?,?,?,?)";
    private static final String UPDATE_ADULTO = "UPDATE participante SET nombre = ?, edad = ?, sugerencia_regalo = ?, id_amigo_secreto = ?, trabajador_activo = ? WHERE id = ? ";
    private static final String UPDATE_NINO = "UPDATE participante SET nombre = ?, edad = ?, sugerencia_regalo = ?, id_amigo_secreto = ?, nombre_tutor = ? WHERE id = ? ";
    private static final String DELETE = "DELETE FROM participante WHERE id = ?";

    public MySQLParticipanteDAO(){
    }

    @Override
    public List<Participante> obtenerTodos() throws PersistenciaException {
        ArrayList<Participante> participantes = new ArrayList<>();
        try(Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(SELECT_ALL);
        ){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                participantes.add(ParticipanteMapper.mapear(rs));
            }

        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener participantes: " + e.getMessage(), e);
        }

        return participantes;
    }

    @Override
    public void insertar(Participante participante) throws PersistenciaException {
        //String sql = (participante instanceof Adulto)?INSERT_ADULTO:INSERT_NINO;
        String sql;
        if(participante instanceof Adulto){
            sql = INSERT_ADULTO;
        }else{
            sql = INSERT_NINO;
        }
        try(
            Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1,participante.getNombre());
            ps.setInt(2,participante.getEdad());
            ps.setString(3, participante.getSugerenciaRegalo());
            if(participante instanceof Adulto){
                Adulto adulto = (Adulto) participante;
                ps.setBoolean(4,adulto.isTrabajadorActivo());
            }else{
                Nino nino = (Nino) participante;
                ps.setString(4,nino.getNombreTutor());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al insertar participante: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizar(Participante participante) throws PersistenciaException {
        String sql = (participante instanceof Adulto)?UPDATE_ADULTO:UPDATE_NINO ;
        try(
                Connection con = ConexionBD.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ){
            ps.setString(1,participante.getNombre());
            ps.setInt(2,participante.getEdad());
            ps.setString(3, participante.getSugerenciaRegalo());
            if (participante.tieneAmigoSecreto()) {
                ps.setInt(4, participante.getIdAmigoSecreto());
            } else {
                ps.setNull(4, Types.INTEGER); // 0 = sin sorteo -> NULL
            }
            if(participante instanceof Adulto){
                Adulto adulto = (Adulto) participante;
                ps.setBoolean(5,adulto.isTrabajadorActivo());
            }else{
                Nino nino = (Nino) participante;
                ps.setString(5,nino.getNombreTutor());
            }
            ps.setInt(6,participante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar participante: " + e.getMessage(), e);
        }
    }

    @Override
    public void eliminar(int id) throws PersistenciaException {
        try(Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE);
        ){
            ps.setInt(1,id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar participante: " + e.getMessage(), e);
        }
    }

    public void eliminar(Participante participante) throws PersistenciaException {
        try(Connection con = ConexionBD.getConnection();
            PreparedStatement ps = con.prepareStatement(DELETE);
        ){
            ps.setInt(1,participante.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar participante: " + e.getMessage(), e);
        }
    }
}
