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

/**
 * A factory class that provides different implementations for {@link LineCreator}
 * 
 * @author Lasse Osterhagen
 *
 */

public class LineCreatorFactory {

	private static Map<String, LineCreatorProvider> localLineCreatorProviders() {
		return Collections.unmodifiableMap(Stream.of(
		new SimpleEntry<>("GENERIC_RAND", new GenericWordLineCreatorProvider("GENERIC_RAND",
			(p,s)->new RandWordCreator(p), Distributions.rightSkewed)),
		new SimpleEntry<>("GENERIC_RAND_LANG", new GenericWordLineCreatorProvider("GENERIC_RAND_LANG",
			(p,s)->new RandLangWordCreator(p), Distributions.rightSkewed)),
		new SimpleEntry<>("ADAPT_RAND", new GenericWordLineCreatorProvider("ADAPT_RAND",
			(p,s)->new AdaptRandWordCreator(p, s), Distributions.rightSkewed)),
		new SimpleEntry<>("ADAPT_RAND_LANG", new GenericWordLineCreatorProvider("ADAPT_RAND_LANG",
				(p,s)->new AdaptRandLangWordCreator(p, s), Distributions.rightSkewed)),
		new SimpleEntry<>("wordList", new WordListLineCreatorProvider()),
		new SimpleEntry<>("text", new TextLineCreatorProvider())
		)
		.collect(Collectors.toMap((e) -> e.getKey(), e -> e.getValue())));
	}
	
	/**
	 * Get a {@link LineCreatorProvider} that can provide a {@link LineCreator} of the specified
	 * type. The type parameter corresponds to the <i>lineCreatorType</i> property of
	 * {@link trainer.Exercise}.
	 * @param type the type of the LineCreator that the LineCreatorProvider should provide
	 * @return the implementation of LineCreatorProvider
	 * @throws ImplementationNotFound if an implementation of LineCreatorProvider for the specified
	 * LineCreator type cannot be found
	 */
	public static LineCreatorProvider getLineCreatorProvider(String type) throws ImplementationNotFound {
		return localLineCreatorProviders().containsKey(type) ?
				localLineCreatorProviders().get(type) : findPluginLineCreatorProvider(type);
	}
	
	/**
	 * Construct a <code>LineCreator</code> of the specified type.
	 * @param type the type of the LineCreator. Corresponds to the property <i>lineCreatorType</i>
	 * of an {@link trainer.Exercise}
	 * @param param parameters that specify the behavior of the resulting LineCreator
	 * @param ps a reference to the ongoing performance of the typist, which may affect the
	 * behavior of the LineCreator
	 * @return the initialized LineCreator
	 * @throws ImplementationNotFound if no implementation of LineCreator for the specified type
	 * could be found
	 * @throws InitException if an error occurred during initializing of the LineCreator (e. g.
	 * if some resources like an Internet connection are not available).
	 */
	public static LineCreator getLineCreator(String type, String param, PerformanceStats ps)
			throws ImplementationNotFound, InitException {
		return getLineCreatorProvider(type).getLineCreator(param, ps);
	}
	
	/**
	 * List all available {@link LineCreator} types. These are all local <code>LineCreator</code>s,
	 * which reside inside the .jar package, and those that reside inside the plugin directory.
	 * @return all available LineCreator types
	 */
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
	
	/**
	 * This exception is thrown, if an implementation for a {@link LineCreator} could
	 * not be found. Typically this will happen, if an entry of {@link trainer.Exercise} in
	 * the database specifies a <code>LineCreator</code> type, that does not exist either
	 * locally inside the .jar or as a plugin inside the plugin directory.
	 */
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
