/**
 * Copyright 2011 eBusiness Information, Groupe Excilys (www.excilys.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.excilys.ebi.gatling.statistics.template

import com.excilys.ebi.gatling.core.util.StringHelper._
import com.excilys.ebi.gatling.statistics.series.Series
import com.excilys.ebi.gatling.statistics.series.YAxis

class GlobalRequestsTemplate(val runOn: String, val series: List[Series]) {

	def getOutput: String = {
		val highstocks = new HighstocksTimeTemplate(series, "Number of requests per second", List(new YAxis("Number of requests per second", "req", false)), "{} requests").getOutput
		new LayoutTemplate("Requests per second", runOn, EMPTY, highstocks).getOutput
	}

}