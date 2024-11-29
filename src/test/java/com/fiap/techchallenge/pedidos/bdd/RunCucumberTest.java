package com.fiap.techchallenge.pedidos.bdd;

import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.context.annotation.Profile;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@SelectClasspathResource("com.fiap.techchallenge.pedidos.bdd")
public class RunCucumberTest {
}
