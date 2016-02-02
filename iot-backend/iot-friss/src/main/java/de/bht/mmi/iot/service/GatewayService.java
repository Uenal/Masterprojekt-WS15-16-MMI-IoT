package de.bht.mmi.iot.service;

import de.bht.mmi.iot.exception.EntityNotFoundException;
import de.bht.mmi.iot.exception.NotAuthorizedException;
import de.bht.mmi.iot.model.Gateway;
import org.springframework.security.core.userdetails.UserDetails;

public interface GatewayService {

    Iterable<Gateway> getAll();

    Iterable<Gateway> getAllForIds(String... gatewayId);

    Iterable<Gateway> getAllByOwner(String username);

    Gateway getGateway(String gatewayId) throws EntityNotFoundException;

    Gateway createGateway(Gateway gateway) throws EntityNotFoundException;

    Gateway updateGateway(String gatewayId, Gateway gateway, UserDetails authenticatedUser)
            throws EntityNotFoundException, NotAuthorizedException;

    void deleteGateway(String gatewayId) throws EntityNotFoundException;
}
