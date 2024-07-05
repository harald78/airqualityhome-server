#!/bin/bash
export flyway_user=admin
export flyway_user_password=<your-admin-pw>
export jwt_secret=<a-sha-256-secret>
export mariaDB_database=airqualityhome
export mariaDB_url=jdbc:mariadb://<host>:3306/airqualityhome
export mariaDB_user=user
export mariaDB_user_password=<your-user-pw>
export SPRING_PROFILES_ACTIVE=prod
export sensor_api_auth_token=<a-secret-auth-token>
export sensor_api_token_header_name=X-API-KEY
export vapid_public_key=<vapid-public-key>
export vapid_private_key=<vapid-private-key>