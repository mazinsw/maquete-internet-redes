package router;

/**
 * Interface para comunica��o de status de envio de pacotes
 * @author Francimar
 *
 */
public interface DispatcherListener {

    /**
     * M�todo chamado quando um pacote n�o � enviado devido a uma falha de 
     * comunica��o ou ao estourar o buffer de entrada
     * 
     * @param data dados descartado ou que n�o foi poss�vel envi�-lo
     */
    public void lostData(pkg.Package data);
    
    /**
     * M�todo chamado quando um pacote � enviado com sucesso para o destinat�rio
     * 
     * @param buffer buffer enviado com sucesso para o destinat�rio
     */
    public void sentPackage(pkg.Package buffer);
}
