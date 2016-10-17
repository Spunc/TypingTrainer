package trainer.lineCreators;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import trainer.PerformanceStats;


public class LineCreatorFactory {
	
	private static Map<String, LineCreatorProvider> localLineCreatorProviders() {
		// Just to remember why we need two times the same String:
		// First: the name as found in field of Exercise.lineCreatorType
		// Second: the key as found in resources.lineCreatorText.properties
		return Collections.unmodifiableMap(Stream.of(
		new SimpleEntry<>("GENERIC_RAND", new GenericWordLineCreatorProvider("GENERIC_RAND",
			(p,s)->new RandWordCreator(p), Distributions.rightSkewed)),
		new SimpleEntry<>("GENERIC_RAND_LANG", new GenericWordLineCreatorProvider("GENERIC_RAND_LANG",
			(p,s)->new RandLangWordCreator(p), Distributions.rightSkewed)),
		new SimpleEntry<>("ADAPT_RAND", new GenericWordLineCreatorProvider("ADAPT_RAND",
			(p,s)->new AdaptRandWordCreator(p, s), Distributions.rightSkewed)),
		new SimpleEntry<>("ADAPT_RAND_LANG", new GenericWordLineCreatorProvider("ADAPT_RAND_LANG",
				(p,s)->new AdaptRandLangWordCreator(p, s), Distributions.rightSkewed)),
		new SimpleEntry<>("wordList", new FileLineCreatorProvider("wordList",
				is-> new WordListLineCreator(is)))
		)
		.collect(Collectors.toMap((e) -> e.getKey(), e -> e.getValue())));
	}
	
	public static LineCreatorProvider getLineCreatorProvider(String type) throws ImplementationNotFound {
		return localLineCreatorProviders().containsKey(type) ?
				localLineCreatorProviders().get(type) : findPluginLineCreatorProvider(type);
	}
	
	public static LineCreator getLineCreator(String type, String param, PerformanceStats ps)
			throws ImplementationNotFound, InitException {
		return getLineCreatorProvider(type).getLineCreator(param, ps);
	}
	
	public static String[] getAvailableLineCreatorTypes() {
		ArrayList<String> l = new ArrayList<>();
		// Add local LineCreator names
		l.addAll(localLineCreatorProviders().keySet());
		// Add plugin LineCreator names
		ServiceLoader<LineCreatorProviderPlugin> loader =
				ServiceLoader.load(LineCreatorProviderPlugin.class);
		Iterator<LineCreatorProviderPlugin> pluginIter = loader.iterator();
		while(pluginIter.hasNext()) {
			LineCreatorProviderPlugin lp = pluginIter.next();
			l.add(lp.getName());
		}
		return l.toArray(new String[0]);
	}
	
	@SuppressWarnings("serial")
	public static class ImplementationNotFound extends Exception {
		public final String pluginType;
		public ImplementationNotFound(String pluginType) {
			super("Could not find plugin resource for type: " + pluginType + ".");
			this.pluginType = pluginType;
		}
	}
	
	private static LineCreatorProvider findPluginLineCreatorProvider(String type)
			throws ImplementationNotFound {
		ServiceLoader<LineCreatorProviderPlugin> loader =
				ServiceLoader.load(LineCreatorProviderPlugin.class);
		Iterator<LineCreatorProviderPlugin> pluginIter = loader.iterator();
		while(pluginIter.hasNext()) {
			LineCreatorProviderPlugin lp = pluginIter.next();
			if(lp.getName().equals(type)) {
				return lp;
			}
		}
		throw new ImplementationNotFound(type);
	}

}
