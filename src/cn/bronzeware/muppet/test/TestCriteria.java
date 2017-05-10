package cn.bronzeware.muppet.test;

import java.util.Iterator;
import java.util.List;

import org.omg.IOP.TransactionService;

import cn.bronzeware.core.ioc.annotation.Component;
import cn.bronzeware.muppet.core.Criteria;
import cn.bronzeware.muppet.core.Session;
import cn.bronzeware.muppet.core.SessionFactory;
import cn.bronzeware.muppet.entities.Note;
import cn.bronzeware.muppet.transaction.Transaction;
import cn.bronzeware.muppet.transaction.TransactionExecute;
import cn.bronzeware.muppet.util.ArrayUtil;
import cn.bronzeware.muppet.util.log.Logger;
import cn.bronzeware.util.testframework.Test;

@Test
@Component
public class TestCriteria extends TestSuper{

	
	@Test
	public  void test1(){

		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Criteria<Note> criteria = session.createCriteria(Note.class);
				criteria.andPropGreater("user_id", 0);
				
				Logger.println("查询结果个数： " + criteria.select("value,pk,password,user_id")
						.list().size());
				return null;
			}
		});
	}
	
	@Test
	public void test2(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Criteria<Note> criteria = session.createCriteria(Note.class);
				criteria.andPropGreaterEq("pk", 1);
				criteria.andPropLess("value", 5);
				List<Note> notes = criteria.list();
				Logger.println("查询结果:");
				ArrayUtil.println(notes);
				return null;
			}
		});
		
	}
	@Test
	public void test3(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Criteria<Note> criteria = session.createCriteria(Note.class);
				criteria.andPropLessEq("username", 7)
						.andPropLike("value", "%6%");
				List<Note> notes = criteria.list();
				ArrayUtil.println(notes);
				return null;
			}
		});
	}
	@Test
	public void test4(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Criteria<Note> criteria = session.createCriteria(Note.class);
				criteria.andPropNotEqual("pk", 3);
				List<Note> notes = criteria.list();
				Logger.println("查询结果:");
				ArrayUtil.println(notes);
				return null;
			}
		});
	}
	@Test
	public void test5(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Criteria<Note> criteria = session.createCriteria(Note.class);
				criteria.order("pk", false);
				List<Note> notes = criteria.list();
				ArrayUtil.println(notes);
				return null;
			}
		});
	}
	@Test
	public void test6(){
		sessionFactory.transactionOperationCallbackTest(new TransactionExecute() {
			
			@Override
			public Object execute(Session session, Transaction transaction) {
				Criteria<Note> criteria = session.createCriteria(Note.class);
				criteria.andPropLess("pk", 3);
				Criteria<Note> criteria2 = session.createCriteria(Note.class);
				criteria2.andPropGreater("pk", 5);
				criteria.or(criteria2);
				List<Note> notes = criteria.list();
				ArrayUtil.println(notes);
				return null;
			}
		});
	}
	
	
	
	public static void main(String[] args){
		TestCriteria criteria = new TestCriteria();
		criteria.test1();
	}
	
}
