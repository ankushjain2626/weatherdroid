
/* 
 * Copyright (C) 2008 Rajab Ma <majianle@gmail.com>
 * http://www.tearcry.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * 
 */

package cn.tearcry.api.weather;

/**
 * 
 * @author  Rajab Ma<majianle@gmail.com>
 *
 */
public class WeatherException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3426858314483845810L;
	private String detailedInfo;
	
	

	/**
	 * @param arg0
	 */
	public WeatherException(String arg0) {
		super(arg0);
		detailedInfo=arg0;
		
	}

	public String toString() {
		return detailedInfo;
	}

}
