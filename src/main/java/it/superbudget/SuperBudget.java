package it.superbudget;

import it.superbudget.gui.SplashScreen;
import it.superbudget.util.messages.MessagesUtils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Set;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

/**
 * Main class for the application
 * 
 * @author Francesco Muià
 * 
 */
public class SuperBudget
{
	private static boolean getMonitoredVMs(int processPid)
	{
		MonitoredHost host;
		Set<?> vms;
		try
		{
			host = MonitoredHost.getMonitoredHost(new HostIdentifier((String) null));
			vms = host.activeVms();
		}
		catch (java.net.URISyntaxException sx)
		{
			throw new InternalError(sx.getMessage());
		}
		catch (MonitorException mx)
		{
			throw new InternalError(mx.getMessage());
		}
		MonitoredVm mvm = null;
		String processName = null;
		try
		{
			mvm = host.getMonitoredVm(new VmIdentifier(String.valueOf(processPid)));
			processName = MonitoredVmUtil.commandLine(mvm);
			processName = processName.substring(processName.lastIndexOf("\\") + 1, processName.length());
			mvm.detach();
		}
		catch (Exception ex)
		{

		}
		for (Object vmid : vms)
		{
			if (vmid instanceof Integer)
			{
				int pid = ((Integer) vmid).intValue();
				String name = vmid.toString(); // default to pid if name not available
				try
				{
					mvm = host.getMonitoredVm(new VmIdentifier(name));
					// use the command line as the display name
					name = MonitoredVmUtil.commandLine(mvm);
					name = name.substring(name.lastIndexOf("\\") + 1, name.length());
					mvm.detach();
					if ((name.equalsIgnoreCase(processName)) && (processPid != pid))
						return false;
				}
				catch (Exception x)
				{
					// ignore
				}
			}
		}

		return true;
	}

	public static void main(String[] args) throws Exception
	{
		RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
		final int runtimePid = Integer.parseInt(runtimeMXBean.getName().substring(0, runtimeMXBean.getName().indexOf("@")));
		if (!SuperBudget.getMonitoredVMs(runtimePid))
		{
			MessagesUtils.showErrorMessage(null, "Esiste già un'altra instanza dell'applicativo");
		}
		else
		{

			Runtime.getRuntime().addShutdownHook(new ShutdownHook());
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			SwingUtilities.invokeLater(new Runnable()
			{

				public void run()
				{
					SplashScreen splashScreen = new SplashScreen();
					splashScreen.setVisible(true);
					splashScreen.start();

				}
			});
		}

	}
}
