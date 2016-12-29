package cn.bronzeware.muppet.util.autogenerate;

import cn.bronzeware.muppet.core.XMLConfig;



/**
 * 自动代码生成解析接口，这个接口并不定义具体解析输入的细节，也没有定义输入方式，输入条件，只定义了
 * <br/><br/>
 * 获取输出，即要获取信息的接口，{@link AutoGenerate}是代码生成接口，{@link AutoInfo}是它的输入
 * <br/><br/>
 * @author 于海强
 *
 */
 interface AutoGenerateConfig extends XMLConfig{

	public AutoInfo getAutoInfo();
}
