package com.example.mysql.sql.Device;


import com.example.mysql.device.DeviceDao;
import com.example.mysql.domain.Device;
import com.example.mysql.entity.sql.DeviceEntity;
import com.example.mysql.sql.JpaAbstractDao;
import com.example.mysql.util.SqlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
@SqlDao
public class JpaDeviceDao extends JpaAbstractDao<DeviceEntity, Device> implements DeviceDao {

    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    protected Class<DeviceEntity> getEntityClass() {
        return DeviceEntity.class;
    }

    @Override
    protected CrudRepository<DeviceEntity, Long> getCrudRepository() {
        return deviceRepository;
    }



}
