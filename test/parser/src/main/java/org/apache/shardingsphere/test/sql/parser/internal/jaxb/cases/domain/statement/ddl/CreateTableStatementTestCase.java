/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.statement.ddl;

import lombok.Getter;
import lombok.Setter;
import org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.segment.impl.column.ExpectedColumn;
import org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.segment.impl.definition.ExpectedColumnDefinition;
import org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.segment.impl.definition.ExpectedConstraintDefinition;
import org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.segment.impl.table.ExpectedSimpleTable;
import org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.statement.SQLParserTestCase;
import org.apache.shardingsphere.test.sql.parser.internal.jaxb.cases.domain.statement.dml.SelectStatementTestCase;

import javax.xml.bind.annotation.XmlElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Create table statement test case.
 */
@Getter
@Setter
public final class CreateTableStatementTestCase extends SQLParserTestCase {
    
    @XmlElement
    private ExpectedSimpleTable table;
    
    @XmlElement(name = "column-definition")
    private final List<ExpectedColumnDefinition> columnDefinitions = new LinkedList<>();
    
    @XmlElement(name = "constraint-definition")
    private final List<ExpectedConstraintDefinition> constraintDefinitions = new LinkedList<>();
    
    @XmlElement(name = "column")
    private final List<ExpectedColumn> columns = new LinkedList<>();
    
    @XmlElement(name = "select")
    private SelectStatementTestCase createTableAsSelectStatement;
}
