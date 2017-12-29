package i5b5.daniel.serszen.pz.model.mybatis.typehandlers;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.Calendar;

public class DateCalendarTypeHandler implements TypeHandler<Calendar>{
    @Override
    public void setParameter(PreparedStatement ps, int i, Calendar parameter, JdbcType jdbcType) throws SQLException {
        if(parameter == null){
            ps.setNull(i, Types.TIMESTAMP);
        }else{
            ps.setTimestamp(i, new Timestamp(parameter.getTime().getTime()));
        }
    }

    @Override
    public Calendar getResult(ResultSet rs, String columnName) throws SQLException {
        return mapToCalendar(rs.getTimestamp(columnName));
    }

    @Override
    public Calendar getResult(ResultSet rs, int columnIndex) throws SQLException {
        return mapToCalendar(rs.getTimestamp(columnIndex));
    }

    @Override
    public Calendar getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return mapToCalendar(cs.getTimestamp(columnIndex));
    }

    private Calendar mapToCalendar(Timestamp date) {
        if(date == null){
            return null;
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        }
    }
}
