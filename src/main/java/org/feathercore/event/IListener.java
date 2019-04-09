package org.feathercore.event;

/**
 * Events could be handled (and their handle-methods could be marked with @EventHandler) only
 * inside of classes implementing IListener. Also, you need to call EventManager.registerListener(IListener)
 * in order to make your handle-methods work.
 *
 * Created by k.shandurenko on 09/04/2019
 */
public interface IListener {}