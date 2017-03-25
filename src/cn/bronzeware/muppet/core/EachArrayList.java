package cn.bronzeware.muppet.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class EachArrayList<E> extends ArrayList<E> {

	private int start = 0;
	private int end = 0;
	
	private final int EACH_SIZE = Criteria.EACH_SIZE;

	private int size;

	private final String UNSUPPORT_OPERATION_EXECEPTION_MSG = "Batch collection of short duration does not support this operation";

	private AbstractCriteria<E> criteria;

	private List<E> list(long start, int offset){
		AbstractCriteria abstractCriteria = (AbstractCriteria) (criteria.limit(start, offset));
		return abstractCriteria.list(false);
	}
	
	public EachArrayList(AbstractCriteria<E> criteria, int size) {
		super();
		this.criteria = criteria;
		this.size = size;
		List<E> list = list(start, EACH_SIZE);
		end = EACH_SIZE;
		super.addAll(list);
	}



	@Override
	public void clear() {
		super.clear();
		start = 0;
		end = 0;
	}

	@Override
	public Object clone() {

		return super.clone();
	}

	@Override
	public void ensureCapacity(int minCapacity) {

		super.ensureCapacity(minCapacity);
	}

	@Override
	public E get(int index) {
		if (index >= start + EACH_SIZE || index >= 0 && index < start) {
			this.clear();
			start = index;
			end = start + EACH_SIZE;
			List<E> list = list(start, EACH_SIZE);
			super.addAll(list);
		}
		return super.get(index - start);
	}

	private class Itr implements Iterator<E> {
		private int cursor;
		private int lastRet = -1;

		public boolean hasNext() {
			return cursor != size;
		}

		public E next() {
			int i = cursor;
			if (i >= size)
				throw new NoSuchElementException();
			E e = EachArrayList.this.get(cursor);
			cursor++;
			lastRet = i;
			return e;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
		}
	}

	@Override
	public int indexOf(Object o) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean isEmpty() {
		return size > 0 ? true : false;
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	@Override
	public int lastIndexOf(Object o) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public ListIterator<E> listIterator() {
		return super.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return super.listIterator(index);
	}

	@Override
	public E remove(int index) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public int size() {
		return size;
	}

	static void subListRangeCheck(int fromIndex, int toIndex, int size) {
		if (fromIndex < 0)
			throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
		if (toIndex > size)
			throw new IndexOutOfBoundsException("toIndex = " + toIndex);
		if (fromIndex > toIndex)
			throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		subListRangeCheck(fromIndex, toIndex, size);
		if (fromIndex >= start && toIndex <= (start + EACH_SIZE)) {
			return super.subList(fromIndex, toIndex);
		}else{
			return list(fromIndex, toIndex);
		}
	}

	@Override
	public Object[] toArray() {
		if(start == 0 && end == size){
			return super.toArray();
		}else{
			List<E> list = list(0, size);
			clearAllAndAdd(list, 0, size);
			return super.toArray();
		}
	}

	private void clearAllAndAdd(List<E> list, int start, int end){
		super.clear();
		this.start = start;
		this.end = end;
		super.addAll(list);
	}
	
	
	@Override
	public <T> T[] toArray(T[] a) {
		if(start == 0 && end == size){
			return super.toArray(a);
		}else{
			List<E> list = list(0, size);
			clearAllAndAdd(list, 0, size);
			return super.toArray(a);
		}
	}

	@Override
	public void trimToSize() {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}
	@Override
	public boolean add(E e) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
		// return super.add(e);
	}

	@Override
	public void add(int index, E element) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException(UNSUPPORT_OPERATION_EXECEPTION_MSG);
	}
}
