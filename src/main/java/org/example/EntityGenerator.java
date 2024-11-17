package org.example;


import static org.apache.maven.plugins.annotations.ResolutionScope.COMPILE_PLUS_RUNTIME;

import java.util.List;
import java.util.Map;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.example.model.schema.Column;
import org.example.model.schema.Constraint;
import org.example.schema.AbstractSchemaScanner;
import org.example.schema.SchemaScannerFactory;
import org.example.service.EntityNormalizerService;
import org.example.service.TemplateService;
import org.example.service.impl.FtlTemplateService;
import org.example.service.impl.GenericEntityNormalizerService;

@Mojo(name = "entities-generate", requiresDependencyResolution = COMPILE_PLUS_RUNTIME, defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public final class EntityGenerator extends AbstractGeneratorContext {
	private final TemplateService service = new FtlTemplateService();
	private final EntityNormalizerService normalizer = new GenericEntityNormalizerService();

	@Override
	public void doExecute() {
		log.info(">>> Scanning schema! <<<\n");
		AbstractSchemaScanner scanner = SchemaScannerFactory.of(connectionManager);
		Map<String, List<Constraint>> primaryKeys = scanner.scanTablePrimaryKeys();
		Map<String, List<Column>> columns = scanner.scanTableColumns();
		Map<String, List<Constraint>> foreignKeys = scanner.scanTableForeignKeys();
		Map<String, List<Column>> viewColumns = scanner.scanViews();

		log.info(">>> Generating Java files! <<<\n");
		service.process(normalizer.normalizeEntities(primaryKeys, columns, foreignKeys, viewColumns), this.outputDirectory);
	}
}
