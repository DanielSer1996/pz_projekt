package i5b5.daniel.serszen.pz.model.mybatis.typehandlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.Calendar;

public class DateCalendarTypeHandler implements TypeHandler<String>{
    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null){
            ps.setNull(i, Types.TIMESTAMP);
        }else{
            ps.setString(i, "");
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return mapToCalendar(rs.getTimestamp(columnName));
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return mapToCalendar(rs.getTimestamp(columnIndex));
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return mapToCalendar(cs.getTimestamp(columnIndex));
    }

    private String mapToCalendar(Timestamp date) {
        if(date == null){
            return null;
        }else{
            return date.toString().replaceAll(" (\\d{2}:?){3}.\\d","");
        }
    }
}
