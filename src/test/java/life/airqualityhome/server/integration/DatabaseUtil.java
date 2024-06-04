package life.airqualityhome.server.integration;

import life.airqualityhome.server.model.MeasurementEntity;
import life.airqualityhome.server.model.RegisterRequestEntity;
import life.airqualityhome.server.model.SensorBaseEntity;
import life.airqualityhome.server.model.SensorBaseSensorTypeEntity;
import life.airqualityhome.server.model.SensorEntity;
import life.airqualityhome.server.model.SensorTypeEntity;
import life.airqualityhome.server.model.UserEntity;
import life.airqualityhome.server.model.UserRoleEntity;
import life.airqualityhome.server.repositories.MeasurementRepository;
import life.airqualityhome.server.repositories.RegisterRequestRepository;
import life.airqualityhome.server.repositories.RoleRepository;
import life.airqualityhome.server.repositories.SensorBaseRepository;
import life.airqualityhome.server.repositories.SensorBaseSensorTypeRepository;
import life.airqualityhome.server.repositories.SensorRepository;
import life.airqualityhome.server.repositories.SensorTypeRepository;
import life.airqualityhome.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Component
public class DatabaseUtil {

    @Autowired
    SensorTypeRepository sensorTypeRepository;

    @Autowired
    private RegisterRequestRepository registerRequestRepository;

    @Autowired
    SensorBaseRepository sensorBaseRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Autowired
    SensorBaseSensorTypeRepository sensorBaseSensorTypeRepository;

    @Autowired
    MeasurementRepository measurementRepository;

    void setupSensorsForTest(){
        UUID uuid1 = UUID.nameUUIDFromBytes("F0F0F0".getBytes());
        UUID uuid2 = UUID.nameUUIDFromBytes("F0F0F1".getBytes());
        SensorEntity sensorEntity1 = SensorEntity.builder()
                                                 .id(1L)
                                                 .sensorBaseSensorTypeId(1L)
                                                 .userId(1L)
                                                 .location("Living room")
                                                 .alarmMin(0.0)
                                                 .alarmMax(0.0)
                                                 .alarmActive(false)
                                                 .uuid(uuid1)
                                                 .build();
        SensorEntity sensorEntity2 = SensorEntity.builder()
                                                 .id(2L)
                                                 .sensorBaseSensorTypeId(2L)
                                                 .userId(1L)
                                                 .location("Living room")
                                                 .alarmMin(0.0)
                                                 .alarmMax(0.0)
                                                 .alarmActive(false)
                                                 .uuid(uuid1)
                                                 .build();
        SensorEntity sensorEntity3 = SensorEntity.builder()
                                                 .id(3L)
                                                 .sensorBaseSensorTypeId(1L)
                                                 .userId(2L)
                                                 .location("Bath room")
                                                 .alarmMin(0.0)
                                                 .alarmMax(0.0)
                                                 .alarmActive(false)
                                                 .uuid(uuid2)
                                                 .build();
        SensorEntity sensorEntity4 = SensorEntity.builder()
                                                 .id(4L)
                                                 .sensorBaseSensorTypeId(2L)
                                                 .userId(2L)
                                                 .location("Bath room")
                                                 .alarmMin(0.0)
                                                 .alarmMax(0.0)
                                                 .alarmActive(false)
                                                 .uuid(uuid2)
                                                 .build();
        sensorRepository.saveAll(List.of(sensorEntity1, sensorEntity2, sensorEntity3, sensorEntity4));
    }

    void setupSensorBaseAndSensorTypeDB() {
        SensorBaseEntity sensorBaseEntity = this.setupAndGetSensorBaseEntitiesForTest();
        sensorBaseRepository.save(sensorBaseEntity);
    }

    void setupSensorBaseSensorTypeRelations() {
        final var sensorBaseSensorType1 = SensorBaseSensorTypeEntity.builder()
                                                                    .id(1L)
                                                                    .sensorTypesId(1L)
                                                                    .sensorBaseEntityId(1L)
                                                                    .build();
        final var sensorBaseSensorType2 = SensorBaseSensorTypeEntity.builder()
                                                                    .id(2L)
                                                                    .sensorTypesId(2L)
                                                                    .sensorBaseEntityId(1L)
                                                                    .build();
        sensorBaseSensorTypeRepository.saveAll(List.of(sensorBaseSensorType1, sensorBaseSensorType2));
    }

    void setupRegisterRequestDB_forUserQuery() {
        SensorBaseEntity sensorBaseEntity = this.setupAndGetSensorBaseEntitiesForTest();
        RegisterRequestEntity entity1 = RegisterRequestEntity.builder()
                                                             .userId(1L)
                                                             .sensorBase(sensorBaseEntity)
                                                             .location("Living room")
                                                             .active(false)
                                                             .build();
        RegisterRequestEntity entity2 = RegisterRequestEntity.builder()
                                                             .userId(2L)
                                                             .sensorBase(sensorBaseEntity)
                                                             .location("Sleeping room")
                                                             .active(true)
                                                             .build();
        RegisterRequestEntity entity3 = RegisterRequestEntity.builder()
                                                             .userId(2L)
                                                             .sensorBase(sensorBaseEntity)
                                                             .location("Bath room")
                                                             .active(false)
                                                             .build();
        registerRequestRepository.saveAll(List.of(entity1, entity2, entity3));
    }

    SensorBaseEntity setupAndGetSensorBaseEntitiesForTest() {
        final var sensorType1 = SensorTypeEntity.builder()
                                                .id(1L)
                                                .type(SensorTypeEntity.Type.TEMPERATURE)
                                                .maxValue(-120.0)
                                                .minValue(-40.0)
                                                .name("SHT30")
                                                .build();
        final var sensorType2 = SensorTypeEntity.builder()
                                                .id(2L)
                                                .type(SensorTypeEntity.Type.GAS)
                                                .maxValue(100.0)
                                                .minValue(100000.0)
                                                .name("MQ-2")
                                                .build();
        sensorTypeRepository.saveAll(List.of(sensorType1, sensorType2));
        return SensorBaseEntity.builder()
                               .id(1L)
                               .name("AZ-Envy")
                               .sensorTypes(Set.of(
                                   sensorType1,
                                   sensorType2
                               )).build();
    }

    void setupUserForTest() {
        UserRoleEntity roleRead = UserRoleEntity.builder().id(1L).name("APP_READ").build();
        UserRoleEntity roleWrite = UserRoleEntity.builder().id(1L).name("APP_WRITE").build();
        this.roleRepository.saveAll(List.of(roleRead, roleWrite));

        UserEntity user1 = UserEntity.builder().id(1L).password("pw1").email("test@test.de").username("user1").roles(Set.of(roleRead, roleWrite)).build();
        UserEntity user2 = UserEntity.builder().id(2L).password("pw2").email("test2@test.de").username("user2").roles(Set.of(roleRead, roleWrite)).build();
        this.userRepository.saveAll(List.of(user1, user2));
    }

    void setupMeasurementsForTest() {
        var random = new Random(42L);
        for (int i = 1; i < 5; i++) {
            List<MeasurementEntity> sensorMeasurements = new ArrayList<>();
            SensorEntity sensorEntity = this.sensorRepository.findFirstById(Integer.toUnsignedLong(i));
            for (int j = 0; j < 10; j++) {
                MeasurementEntity measurementEntity = MeasurementEntity
                    .builder()
                    .unit(i == 1 || i == 3 ? MeasurementEntity.Unit.CELSIUS : MeasurementEntity.Unit.PPM)
                    .sensorId(sensorEntity.getId())
                    .sensorEntity(sensorEntity)
                    .sensorValue(i == 1 || i == 3 ? random.nextDouble(10.0, 40.0) : random.nextDouble(100.0, 10000.0))
                    .timestamp(LocalDateTime.of(2024, 6, 3, 12, j, 0).toInstant(ZoneOffset.UTC))
                    .build();
                sensorMeasurements.add(measurementEntity);
            }
            this.measurementRepository.saveAll(sensorMeasurements);
        }

    }
}
