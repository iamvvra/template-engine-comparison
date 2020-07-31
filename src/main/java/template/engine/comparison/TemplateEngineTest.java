package template.engine.comparison;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import org.stringtemplate.v4.ST;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import httl.Engine;
import httl.Template;

public class TemplateEngineTest {

	private static class Data {
		public Data(String year) {
			this.year9 = year;
		}

		String year9;
	}

	private static final int COUNT = 100_00_0;

	public static void main(String[] args) {
		IntStream.range(1, 3).forEach(i -> {
			System.out.println("run: " + i);
			mustacheTemplate().andThen(thymeLeafTemplate()).andThen(stringTemplate()).andThen(httlTemplate())
					.apply(getData());
		});
	}

	private static Function<Map<String, Object>, Map<String, Object>> mustacheTemplate() {
		return (map) -> {
			MustacheFactory mf = new DefaultMustacheFactory();
			long start = System.currentTimeMillis();
			IntStream.range(0, COUNT).forEach(i -> {
				String file = getTemplateName() + ".template";
				Mustache mustache = mf.compile(file);
				Writer stringWriter = new StringWriter();
				try {
					mustache.execute(stringWriter, new Data(map.get("year4").toString())).flush();
					// System.out.println(stringWriter);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			print("mustache: ", start, System.currentTimeMillis());
			return map;
		};
	}

	private static Function<Map<String, Object>, Map<String, Object>> httlTemplate() {
		return (map) -> {
			long start = System.currentTimeMillis();
			Properties configProperties = new Properties();
			configProperties.setProperty("template.directory",
					"/Users/vijayveeraraghavan/git/template-engine-comparison/src/main/resources");
			configProperties.setProperty("template.suffix", ".httl");
			configProperties.setProperty("reloadable", "true");
			configProperties.setProperty("loaders", "httl.spi.loaders.FileLoader");
			Engine engine = Engine.getEngine(configProperties);
			IntStream.range(0, COUNT).forEach(i -> {
				try {
					Template template = engine.getTemplate(getTemplateName() + ".httl");
					StringWriter stringWriter = new StringWriter();
					template.render(map, stringWriter);
					// System.out.println(stringWriter.toString());
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
			});
			print("httl: ", start, System.currentTimeMillis());
			return map;
		};
	}

	private static Function<Map<String, Object>, Map<String, Object>> thymeLeafTemplate() {
		return (map) -> {
			long start = System.currentTimeMillis();
			TemplateEngine engine = new TemplateEngine();
			engine.setTemplateResolver(getResolver());
			IntStream.range(0, COUNT).forEach(e -> {
				Context context = new Context();
				context.setVariables(map);
				String str = engine.process(getTemplateName(), context);
				// System.out.println(str);
			});
			print("thymeleaf: ", start, System.currentTimeMillis());
			return map;
		};
	}

	private static ITemplateResolver getResolver() {
		FileTemplateResolver templateResolver = new FileTemplateResolver();
		// templateResolver.setOrder(Integer.valueOf(1));
		// templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
		File file = new File("src/main/resources/");
		templateResolver.setPrefix(file.getAbsolutePath() + File.separator);
		templateResolver.setSuffix(".st");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		// templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(false);
		return templateResolver;
	}

	private static Function<Map<String, Object>, Map<String, Object>> pebbleTemplate() {

		return (map) -> {
			long start = System.currentTimeMillis();
			Writer writer = new StringWriter();
			PebbleEngine engine = new PebbleEngine.Builder().build();
			PebbleTemplate compiledTemplate = engine.getTemplate("src/main/resources/template2.html");
			IntStream.range(0, COUNT).forEach(e -> {
				try {
					compiledTemplate.evaluate(writer, map);
					String string = writer.toString();
					// System.out.println(string);
				} catch (IOException e1) {
				}
			});
			print("pebble: ", start, System.currentTimeMillis());
			return map;
		};
	}

	private static Function<Map<String, Object>, Map<String, Object>> stringTemplate() {
		return (map) -> {
			long start = System.currentTimeMillis();
			IntStream.range(0, COUNT).forEach(i -> {
				ST st = new ST(getTemplate(), '{', '}');
				map.entrySet().stream().forEach(e -> st.add(e.getKey(), e.getValue()));
				String render = st.render();
				// System.out.println(render);
			});
			print("stringtemplate: ", start, System.currentTimeMillis());
			return map;
		};

	}

	private static void print(String string, long start, long end) {
		System.out.println(string + ": " + (end - start) + " ms");
	}

	private static Map<String, Object> getData() {
		Map<String, Object> context = new HashMap<>();
		String uuid = UUID.randomUUID().toString();
		context.put("title", uuid.substring(33));
		context.put("name", uuid.substring(25));
		context.put("year", new Random().nextInt(9999));
		context.put("page", uuid.substring(0, 10));
		context.put("page2", uuid.substring(0, 10));
		context.put("page2", uuid.substring(0, 10));
		context.put("page12", uuid.substring(0, 10));
		context.put("page112", uuid.substring(0, 10));
		context.put("page32", uuid.substring(0, 10));
		context.put("page5442", uuid.substring(0, 10));
		context.put("page62", uuid.substring(0, 10));
		context.put("page782", uuid.substring(0, 10));
		context.put("page442", uuid.substring(0, 10));
		context.put("page322", uuid.substring(0, 10));
		context.put("page82", uuid.substring(0, 10));
		context.put("year1", uuid.substring(0, 10));
		context.put("year1", uuid.substring(1, 11));
		context.put("year2", uuid.substring(2, 12));
		context.put("year3", uuid.substring(3, 13));
		context.put("year8", uuid.substring(4, 14));
		context.put("year6", uuid.substring(5, 15));
		context.put("year7", uuid.substring(6, 16));
		context.put("year4", uuid.substring(7, 17));
		context.put("year5", uuid.substring(8, 18));
		context.put("year2", uuid.substring(9, 19));
		context.put("year9", uuid.substring(10, 20));
		return context;
	}

	private static String getTemplate() {
		String[] files = { "st_template.3.st", "st_template.1.st", "st_template.2.st" };
		try {
			return new String(Files.readAllBytes(Paths.get("src/main/resources/" + files[new Random().nextInt(3)])));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String getTemplateName() {
		String[] files = { "st_template.3", "st_template.1", "st_template.2" };
		return files[new Random().nextInt(3)];
	}
}