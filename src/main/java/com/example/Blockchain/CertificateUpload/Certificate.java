package com.example.Blockchain.CertificateUpload;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Int256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.5.5.
 */
@SuppressWarnings("rawtypes")
public class Certificate extends Contract {
    private static final String BINARY = "60806040523480156200001157600080fd5b5060405162000b0e38038062000b0e833981810160405260c08110156200003757600080fd5b81019080805160405193929190846401000000008211156200005857600080fd5b838201915060208201858111156200006f57600080fd5b82518660018202830111640100000000821117156200008d57600080fd5b8083526020830192505050908051906020019080838360005b83811015620000c3578082015181840152602081019050620000a6565b50505050905090810190601f168015620000f15780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156200011557600080fd5b838201915060208201858111156200012c57600080fd5b82518660018202830111640100000000821117156200014a57600080fd5b8083526020830192505050908051906020019080838360005b838110156200018057808201518184015260208101905062000163565b50505050905090810190601f168015620001ae5780820380516001836020036101000a031916815260200191505b5060405260200180516040519392919084640100000000821115620001d257600080fd5b83820191506020820185811115620001e957600080fd5b82518660018202830111640100000000821117156200020757600080fd5b8083526020830192505050908051906020019080838360005b838110156200023d57808201518184015260208101905062000220565b50505050905090810190601f1680156200026b5780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156200028f57600080fd5b83820191506020820185811115620002a657600080fd5b8251866001820283011164010000000082111715620002c457600080fd5b8083526020830192505050908051906020019080838360005b83811015620002fa578082015181840152602081019050620002dd565b50505050905090810190601f168015620003285780820380516001836020036101000a031916815260200191505b50604052602001805160405193929190846401000000008211156200034c57600080fd5b838201915060208201858111156200036357600080fd5b82518660018202830111640100000000821117156200038157600080fd5b8083526020830192505050908051906020019080838360005b83811015620003b75780820151818401526020810190506200039a565b50505050905090810190601f168015620003e55780820380516001836020036101000a031916815260200191505b506040526020018051906020019092919050505085600090805190602001906200041192919062000489565b5084600190805190602001906200042a92919062000489565b5083600290805190602001906200044392919062000489565b5082600390805190602001906200045c92919062000489565b5081600490805190602001906200047592919062000489565b508060058190555050505050505062000538565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620004cc57805160ff1916838001178555620004fd565b82800160010185558215620004fd579182015b82811115620004fc578251825591602001919060010190620004df565b5b5090506200050c919062000510565b5090565b6200053591905b808211156200053157600081600090555060010162000517565b5090565b90565b6105c680620005486000396000f3fe608060405234801561001057600080fd5b506004361061002b5760003560e01c80631532f93a14610030575b600080fd5b61003861026a565b60405180806020018060200180602001806020018060200187815260200186810386528c818151815260200191508051906020019080838360005b8381101561008e578082015181840152602081019050610073565b50505050905090810190601f1680156100bb5780820380516001836020036101000a031916815260200191505b5086810385528b818151815260200191508051906020019080838360005b838110156100f45780820151818401526020810190506100d9565b50505050905090810190601f1680156101215780820380516001836020036101000a031916815260200191505b5086810384528a818151815260200191508051906020019080838360005b8381101561015a57808201518184015260208101905061013f565b50505050905090810190601f1680156101875780820380516001836020036101000a031916815260200191505b50868103835289818151815260200191508051906020019080838360005b838110156101c05780820151818401526020810190506101a5565b50505050905090810190601f1680156101ed5780820380516001836020036101000a031916815260200191505b50868103825288818151815260200191508051906020019080838360005b8381101561022657808201518184015260208101905061020b565b50505050905090810190601f1680156102535780820380516001836020036101000a031916815260200191505b509b50505050505050505050505060405180910390f35b60608060608060606000808054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103095780601f106102de57610100808354040283529160200191610309565b820191906000526020600020905b8154815290600101906020018083116102ec57829003601f168201915b5050505050955060018054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156103a65780601f1061037b576101008083540402835291602001916103a6565b820191906000526020600020905b81548152906001019060200180831161038957829003601f168201915b5050505050945060028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104435780601f1061041857610100808354040283529160200191610443565b820191906000526020600020905b81548152906001019060200180831161042657829003601f168201915b5050505050935060038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156104e05780601f106104b5576101008083540402835291602001916104e0565b820191906000526020600020905b8154815290600101906020018083116104c357829003601f168201915b5050505050925060048054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561057d5780601f106105525761010080835404028352916020019161057d565b820191906000526020600020905b81548152906001019060200180831161056057829003601f168201915b50505050509150600554905090919293949556fea265627a7a72315820db6bff58c8ebd8f8f4e323345e694f7a5defa655c8da5e256997bd497ef5a35164736f6c63430005110032";

    public static final String FUNC_GETCERTINFO = "getCertInfo";

    @Deprecated
    protected Certificate(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Certificate(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Certificate(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Certificate(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteFunctionCall<Tuple6<String, String, String, String, String, BigInteger>> getCertInfo() {
        final Function function = new Function(FUNC_GETCERTINFO, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Int256>() {}));
        return new RemoteFunctionCall<Tuple6<String, String, String, String, String, BigInteger>>(function,
                new Callable<Tuple6<String, String, String, String, String, BigInteger>>() {
                    @Override
                    public Tuple6<String, String, String, String, String, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple6<String, String, String, String, String, BigInteger>(
                                (String) results.get(0).getValue(), 
                                (String) results.get(1).getValue(), 
                                (String) results.get(2).getValue(), 
                                (String) results.get(3).getValue(), 
                                (String) results.get(4).getValue(), 
                                (BigInteger) results.get(5).getValue());
                    }
                });
    }

    @Deprecated
    public static Certificate load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Certificate(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Certificate load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Certificate(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Certificate load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Certificate(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Certificate load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Certificate(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Certificate> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _certId, String _certName, String _gettingTime, String _agenceFrom, String _content, BigInteger _hashCode) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_certId),
                new Utf8String(_certName),
                new Utf8String(_gettingTime),
                new Utf8String(_agenceFrom),
                new Utf8String(_content),
                new Int256(_hashCode)));
        return deployRemoteCall(Certificate.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Certificate> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _certId, String _certName, String _gettingTime, String _agenceFrom, String _content, BigInteger _hashCode) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_certId),
                new Utf8String(_certName),
                new Utf8String(_gettingTime),
                new Utf8String(_agenceFrom),
                new Utf8String(_content),
                new Int256(_hashCode)));
        return deployRemoteCall(Certificate.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Certificate> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _certId, String _certName, String _gettingTime, String _agenceFrom, String _content, BigInteger _hashCode) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_certId),
                new Utf8String(_certName),
                new Utf8String(_gettingTime),
                new Utf8String(_agenceFrom),
                new Utf8String(_content),
                new Int256(_hashCode)));
        return deployRemoteCall(Certificate.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Certificate> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _certId, String _certName, String _gettingTime, String _agenceFrom, String _content, BigInteger _hashCode) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_certId),
                new Utf8String(_certName),
                new Utf8String(_gettingTime),
                new Utf8String(_agenceFrom),
                new Utf8String(_content),
                new Int256(_hashCode)));
        return deployRemoteCall(Certificate.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }
}
