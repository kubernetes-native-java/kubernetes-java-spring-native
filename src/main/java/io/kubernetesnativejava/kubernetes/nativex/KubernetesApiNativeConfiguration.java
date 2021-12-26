package io.kubernetesnativejava.kubernetes.nativex;

import com.google.gson.annotations.JsonAdapter;
import io.swagger.annotations.ApiModel;
import org.reflections.Reflections;
import org.springframework.aot.context.bootstrap.generator.infrastructure.nativex.NativeConfigurationRegistry;
import org.springframework.nativex.AotOptions;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.type.NativeConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.nativex.hint.TypeAccess.*;

/**
 * These hints are inspired by <a href="https://github.com/scratches/spring-controller">
 * Dr. Dave Syer's sample Kubernetes controller</a> and the configuration therein.
 * <p>
 * These types work <a href="https://github.com/kubernetes-client/java">in conjunction
 * with the autoconfiguration provided by the official Kubernetes Java client</a>, most of
 * which is code-generated from Swagger. This support automatically registers any
 * code-generated types that have {@link io.swagger.annotations.ApiModel} on it, limiting
 * the registration to the code-generated types in the {@link io.kubernetes} package.
 * <p>
 * This hints class also registers options required to use this with a HTTPS API endpoints
 * with custom character sets.
 *
 * @author Josh Long
 * @author Dave Syer
	* @author Christian Tzolov
 */

@NativeHint(//

		options = { "-H:+AddAllCharsets", "--enable-all-security-services", "--enable-https", "--enable-http" },
		types = { //
				@TypeHint( //
						access = { DECLARED_CLASSES, DECLARED_CONSTRUCTORS, DECLARED_FIELDS, DECLARED_METHODS }, //
						typeNames = { "io.kubernetes.client.informer.cache.ProcessorListener",
								"io.kubernetes.client.extended.controller.Controller",
								"io.kubernetes.client.custom.IntOrString",
								"io.kubernetes.client.custom.Quantity$QuantityAdapter",
								"io.kubernetes.client.custom.IntOrString$IntOrStringAdapter",
								"io.kubernetes.client.util.generic.GenericKubernetesApi$StatusPatch",
								"io.kubernetes.client.util.Watch$Response",
								"io.kubernetes.client.custom.V1Patch$V1PatchAdapter" }) //
		}//
)
public class KubernetesApiNativeConfiguration implements NativeConfiguration {

	@Override
	public void computeHints(NativeConfigurationRegistry registry, AotOptions aotOptions) {
		Reflections reflections = new Reflections("io.kubernetes");
		Set<Class<?>> apiModels = reflections.getTypesAnnotatedWith(ApiModel.class);
		Set<Class<?>> jsonAdapters = reflections.getTypesAnnotatedWith(JsonAdapter.class);
		Set<Class<?>> all = new HashSet<>();
		all.addAll(jsonAdapters);
		all.addAll(apiModels);
		all.forEach(clzz -> {

			registry.reflection().forType(clzz).withAccess(values()).build();
		});
	}

}
