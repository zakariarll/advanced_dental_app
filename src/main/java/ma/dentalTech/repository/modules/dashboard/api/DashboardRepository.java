package ma.dentalTech.repository.modules.dashboard.api;

public interface DashboardRepository {
    long getNombrePatients();
    long getRDVToday();
    double getChiffreAffaireMois();
    long getAlertesStock(); // Pour les médicaments
}