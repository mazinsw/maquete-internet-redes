package router;

/**
 * Interface para comunicação de status de envio de pacotes
 * @author Francimar
 *
 */
public interface DispatcherListener {

    /**
     * Método chamado quando um pacote não é enviado devido a uma falha de 
     * comunicação ou ao estourar o buffer de entrada
     * 
     * @param data dados descartado ou que não foi possível enviá-lo
     */
    public void lostData(pkg.Package data);
    
    /**
     * Método chamado quando um pacote é enviado com sucesso para o destinatário
     * 
     * @param buffer buffer enviado com sucesso para o destinatário
     */
    public void sentPackage(pkg.Package buffer);
}
