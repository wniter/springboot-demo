package com.example.mysql.sql.Device;

import com.example.mysql.entity.sql.DeviceEntity;
import com.example.mysql.util.SqlDao;
import org.springframework.data.repository.CrudRepository;

@SqlDao
public interface DeviceRepository extends CrudRepository<DeviceEntity, Long> {

}
