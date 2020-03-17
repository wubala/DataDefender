/*
 * Copyright 2014, Armenak Grigoryan, and individual contributors as indicated
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
package com.strider.datadefender.requirement.functions;

/**
 * Specifies that the class contains functions for use by anonymizer functions.
 *
 * This identifies classes that the anonymizer should try to construct, versus
 * classes that identify the return type of a column (and methods that operate
 * on them, for example a function java.lang.String#substring) that should not
 * be constructed.
 *
 * @author Zaahid Bateson <zaahid.bateson@ubc.ca>
 */
public abstract class RequirementFunctionClass {
    
}
