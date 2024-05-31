package life.airqualityhome.server.service;

import life.airqualityhome.server.rest.dto.mapper.SensorBaseMapper;
import life.airqualityhome.server.repositories.SensorBaseRepository;
import life.airqualityhome.server.rest.dto.SensorBaseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegistrationService {

    @Autowired
    private final SensorBaseRepository sensorBaseRepository;

    @Autowired
    private final SensorBaseMapper mapper;

    public List<SensorBaseDto> getAvailableSensorBases() {
        var sensorBases = this.sensorBaseRepository.findAll();
        return StreamSupport.stream(sensorBases.spliterator(), false)
                .map(mapper::toDto)
                .toList();
    }



}
