package router;

import java.rmi.Naming;
import java.util.ArrayList;

import pkg.PackageQueue;

/**
 * Classe resposável por enviar pacotes por demanda para um destinatário, 
 * os pacotes são adicionados em uma fila e enviados um a um, a fila tem um tamanho 
 * máximo de 128 pacotes, ao tentar inserir mais pacotes os mesmos serão descartados
 * 
 * @author Francimar
 * 
 */
public class Dispatcher implements Runnable {

	private PackageQueue queue;
	private boolean running;
	ArrayList<DispatcherListener> list;
	private int processTime;

	/**
	 * Insere um pacote na fila de envio
	 * 
	 * @param pkg
	 *            dados em binário do pacote
	 */
	public void give(pkg.Package pkg) {
		if (queue.size() == queue.getSizeLimit()) {
			// lost packet
			actionLostPackage(pkg);
			return;
		}
		synchronized (queue) {
			queue.push(pkg);
			queue.notify();
		}
	}
	
	public void setTimeProcessing(int time) {
		processTime = time;
	}

	/**
	 * Obtém um pacote da fila de envio, se não existir um elemento o método
	 * espera até que um seja adicionado
	 * 
	 * @return pacote binário em forma de vetor de bytes
	 * @throws InterruptedException
	 */
	private pkg.Package take() throws InterruptedException {
		while (queue.isEmpty()) {
			synchronized (queue) {
				queue.wait();
			}
		}
		return queue.pop();
	}

	/**
	 * Método chamado pela thread para realizar os envios dos pacotes em segundo
	 * plano
	 * 
	 */
	public void run() {
		while (running) {
			pkg.Package pkg;

			try {
				pkg = take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}
			try {
				RouterInterface router = (RouterInterface) Naming.lookup("//" + pkg.getNeighbor().getIP() + "/router");
				router.receive(pkg);
				actionSendData(pkg);
				Thread.sleep(processTime);
			} catch (Exception e) {
				actionLostPackage(pkg);
				continue;
			}
		}
	}

	/**
	 * Cria um dispachador de pacotes sob demanda de envio, o serviço é
	 * automaticamente iniciado
	 */
	public Dispatcher() {
		processTime = 0;
		running = false;
		queue = new PackageQueue();
		list = new ArrayList<DispatcherListener>();
		start();
	}

	/**
	 * Adiciona uma interface de retorno de status do envio de cada pacote
	 * 
	 * @param l
	 *            interface que contém o método de retorno de status
	 */
	public void addListener(DispatcherListener l) {
		list.add(l);
	}

	/**
	 * Para a execução do serviço de envio de pacotes
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Inicia o serviço de entrega de pacotes
	 */
	public void start() {
		if (running) {
			return;
		}
		running = true;
		new Thread(this).start();
	}

	/**
	 * Método auxiliar para avisar aos listeners que um pacote for perdido por
	 * estouro de buffer ou falha no envio
	 * 
	 * @param data buffer que não foi enviado ou foi descartado
	 */
	private void actionLostPackage(pkg.Package data) {
		for (DispatcherListener dl : list) {
			dl.lostData(data);
		}
	}

	/**
	 * Método auxiliar para avisar aos listeners que um pacote for enviado com sucesso
	 * 
	 * @param pkg pacote que foi enviado com sucesso
	 */
	private void actionSendData(pkg.Package pkg) {
		for (DispatcherListener dl : list) {
			dl.sentPackage(pkg);
		}
	}

	public int getQueueSize() {
		return queue.size();
	}

	public void setMaxQueueSize(int size) {
		queue.setSizeLimit(size);
	}
}
