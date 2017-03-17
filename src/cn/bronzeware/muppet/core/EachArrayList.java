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

public class EachArrayList<E> extends ArrayList<E>{

	private int start;
	private final int EACH_SIZE = Criteria.EACH_SIZE;
	
	private int size;
	
	private AbstractCriteria<E> criteria;
	
	
	public  EachArrayList(AbstractCriteria<E> criteria,int size) {
		super();
		this.criteria = criteria;
		this.size = size;
		List<E> list= criteria.limit(start, EACH_SIZE).list();
		super.addAll(list);
	}
	
	
	@Override
	public boolean add(E e) {
		throw  new UnsupportedOperationException("Batch collection of short duration does not support this operation");
		//return super.add(e);
	}

	@Override
	public void add(int index, E element) {
		throw  new UnsupportedOperationException("Batch collection of short duration does not support this operation");
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		
		super.clear();
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
		if(index >= start + EACH_SIZE){
			super.clear();
			start = index;
			List<E> list= criteria.limit(index, EACH_SIZE).list();
			super.addAll(list);
		}else if(index >= 0 && index < start){
			super.clear();
			start = index;
			List<E> list = criteria.limit(start, EACH_SIZE).list();
			super.addAll(list);
		}
		return super.get(index - start);
	}
	
	private class Itr implements Iterator<E>{
		private int cursor ;
		private int lastRet = -1;
		public boolean hasNext(){
			return cursor != size;
		}
		
		public E next(){
			int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            E e = EachArrayList.this.get(cursor);
            cursor ++;
            lastRet = i;
            return e;
		}
		
		
	}
	
	
	

	@Override
	public int indexOf(Object o) {
		
		return super.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		
		return super.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	@Override
	public int lastIndexOf(Object o) {
		
		return super.lastIndexOf(o);
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
		
		return super.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		
		return super.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return super.removeAll(c);
	}

	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		return super.removeIf(filter);
	}

	@Override
	public void replaceAll(UnaryOperator<E> operator) {
		// TODO Auto-generated method stub
		super.replaceAll(operator);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return super.retainAll(c);
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return super.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return super.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return super.toArray(a);
	}

	@Override
	public void trimToSize() {
		super.trimToSize();
	}

	
	
}
