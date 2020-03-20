/**
 * Copyright 2014-2018, Armenak Grigoryan, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package com.strider.datadefender.specialcase;

import com.strider.datadefender.Discoverer.ColumnMatch;
import java.util.ArrayList;
import java.util.List;

import com.strider.datadefender.Probability;
import com.strider.datadefender.database.metadata.TableMetaData.ColumnMetaData;
import com.strider.datadefender.extensions.BiographicFunctions;
import com.strider.datadefender.file.metadata.FileMatchMetaData;
import java.util.Objects;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Armenak Grigoryan
 */
@Log4j2
public class SinDetector implements SpecialCase {

    public static ColumnMatch detectSin(final ColumnMetaData data, final String text) {
        String sinValue = text;

        if (
            StringUtils.isNotBlank(sinValue)
            && (
                Objects.equals(String.class, data.getColumnType())
                || Number.class.isAssignableFrom(data.getColumnType())
            )
        ) {
            final BiographicFunctions bf = new BiographicFunctions();

            if (Objects.equals(String.class, data.getColumnType())) {
                sinValue = sinValue.replaceAll("\\D+", "");
            }

            if (bf.isValidSIN(sinValue)) {
                log.info("SIN detected: " + sinValue + " in " + data.getTable().getTableName() + "." + data.getColumnName());
                return new ColumnMatch(
                    data,
                    1,
                    "sin",
                    List.of(new Probability(sinValue, 1.00))
                );
            }
        }

        return null;
    }
    
    public static FileMatchMetaData detectSin(final FileMatchMetaData metaData, final String text) {
        String sinValue = "";
        
        if (StringUtils.isNotBlank(text)) {
            sinValue = text;
        }

        log.debug("Trying to find SIN in file " + metaData.getFileName() + " : " + sinValue);
        if (isValidSIN(sinValue)) {
                log.info("SIN detected: " + sinValue);
                metaData.setAverageProbability(1.0);
                metaData.setModel("sin");
                return metaData;
        } else {
            log.debug("SIN " + sinValue + " is not valid" );
        }

        return null;
    }    
    
    /**
     * Algorithm is taken from https://en.wikipedia.org/wiki/Social_Insurance_Number
     * @param sin
     * @return boolean true, if SIN is valid, otherwise false
     */
    private static boolean isValidSIN(final String sin) {
        boolean valid = false;

        if (sin.length() < 9) {
            log.debug("SIN length is < 9");

            return valid;
        }

//        if (!sin.matches("[0-9]+")) {
//            LOG.debug("SIN " + sin + " is not number");
//
//            return valid;
//        }

        final int[]         sinArray   = new int[sin.length()];
        final int[]         checkArray = {
            1, 2, 1, 2, 1, 2, 1, 2, 1
        };
        final List<Integer> sinList    = new ArrayList();

        log.debug(sin);

        for (int i = 0; i < 9; i++) {
            sinArray[i] = Integer.valueOf(sin.substring(i, i + 1));
            sinArray[i] = sinArray[i] * checkArray[i];
        }

        int sum = 0;

        for (int i = 0; i < 9; i++) {
            final String tmp = String.valueOf(sinArray[i]);

            if (tmp.length() == 1) {
                sinList.add(Integer.valueOf(tmp));
                sum += Integer.valueOf(tmp);
            } else {
                sinList.add(Integer.valueOf(tmp.substring(0, 1)));
                sum += Integer.valueOf(tmp.substring(0, 1));
                sinList.add(Integer.valueOf(tmp.substring(1, 2)));
                sum += Integer.valueOf(tmp.substring(1, 2));
            }
        }

        if ((sum % 10) == 0) {
            valid = true;
        }

        return valid;
    }    
}