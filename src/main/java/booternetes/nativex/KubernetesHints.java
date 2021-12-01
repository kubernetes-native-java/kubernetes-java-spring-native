package booternetes.nativex;

import lombok.extern.log4j.Log4j2;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;
import org.springframework.nativex.type.AccessDescriptor;
import org.springframework.nativex.type.HintDeclaration;
import org.springframework.nativex.type.NativeConfiguration;
import org.springframework.nativex.type.TypeSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
	*/
@Log4j2
@NativeHint(trigger = io.kubernetes.client.informer.cache.ProcessorListener.class,
	options = {"-H:+AddAllCharsets", "--enable-all-security-services", "--enable-https", "--enable-http"},
	types = {@TypeHint(typeNames = {"io.kubernetes.client.util.Watch$Response"}, access = AccessBits.ALL),
		@TypeHint(typeNames = {"io.kubernetes.client.util.generic.GenericKubernetesApi$StatusPatch"},
			access = AccessBits.ALL),
		@TypeHint(typeNames = {"io.kubernetes.client.custom.Quantity$QuantityAdapter"},
			access = AccessBits.ALL),
		@TypeHint(typeNames = {"io.kubernetes.client.extended.controller.Controller"},
			access = AccessBits.ALL),
		@TypeHint(typeNames = {"io.kubernetes.client.informer.cache.ProcessorListener"},
			access = AccessBits.ALL),
		@TypeHint(typeNames = {"io.kubernetes.client.custom.IntOrString"}, access = AccessBits.ALL),
		@TypeHint(typeNames = {"io.kubernetes.client.custom.IntOrString$IntOrStringAdapter"},
			access = AccessBits.ALL)})
public class KubernetesHints implements NativeConfiguration {


	@Override
	public List<HintDeclaration> computeHints(TypeSystem typeSystem) {
		log.info("Running Kubernetes hints...");
		List<String> models = typeSystem.findTypesAnnotated("Lio/swagger/annotations/ApiModel;", true);
		List<String> adapters = typeSystem.findTypesAnnotated("Lcom/google/gson/annotations/JsonAdapter;", true);
		List<String> rl = new ArrayList<String>();
		rl.addAll(adapters);
		rl.addAll(models);
		return new HashSet<>(rl).stream().filter(clzz -> clzz.startsWith("io/kubernetes")).map(clazzName -> {
			var hd = new HintDeclaration();
			hd.addDependantType(clazzName.replace("/", "."), new AccessDescriptor(AccessBits.ALL));
			return hd;
		}).collect(Collectors.toList());
	}

}
