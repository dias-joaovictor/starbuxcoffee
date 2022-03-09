#!/bin/bash
mysql -u root -p${MYSQL_ROOT_PASSWORD} ${MYSQL_DATABASE} < /dump/script.sql