package org.veronica.core.storage.strategies;

/**
 * Strategy family to decide when data should be flushed to storage sink. 
 * 
 * A combination of flush and shard strategies should be used to get optimal performance.
 * 
 * @author ambudsharma
 *
 */
public abstract class VFlushStrategy extends VStorageStrategy {

}
