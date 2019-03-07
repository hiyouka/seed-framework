package hiyouka.seedframework.beans.metadata;


import javax.annotation.Nullable;

/**
 * @author hiyouka
 * Date: 2019/1/28
 * @since JDK 1.8
 */
public interface ClassMetadata {

   	/**
	 * Return the name of the underlying class.
	 */
	String getClassName();

	/**
	 * Return whether the underlying class represents an interface.
	 */
	boolean isInterface();

	/**
	 * Return whether the underlying class represents an annotation.
	 * @since 4.1
	 */
	boolean isAnnotation();

	/**
	 * Return whether the underlying class is marked as abstract.
	 */
	boolean isAbstract();


	/**
	 * return this class is concrete
	 */
	boolean isConcrete();

	/**
	 * return this class is final
	 */
	boolean isFinal();


	/**
     * not inner class or static inner class
	 */
	boolean isIndependent();

	/**
	 * if this class is internal class return true
	 */
	boolean hasEnclosingClass();

	@Nullable
	String getEnclosingClassName();

	boolean hasSuperClass();

	/**
	 * Return the name of the super class of the underlying class
	 */
	String getSuperClassName();

	/**
	 * Return the names of all interfaces that the underlying class
	 * implements, or an empty array if there are none.
	 */
	String[] getInterfaceNames();

	/**
	 * Return this class all inner className (private static public)
	 */
	String[] getMemberClassNames();

}