import { create } from 'zustand';

interface UiState {
  isSidebarOpen: boolean;
  activeModal: string | null;
  activeTab: string;

  toggleSidebar: () => void;
  openModal: (modalId: string) => void;
  closeModal: () => void;
  setActiveTab: (tab: string) => void;
}

export const useUiStore = create<UiState>((set) => ({
  isSidebarOpen: false,
  activeModal: null,
  activeTab: 'all',

  toggleSidebar: () => set((state) => ({ isSidebarOpen: !state.isSidebarOpen })),
  openModal: (modalId) => set({ activeModal: modalId }),
  closeModal: () => set({ activeModal: null }),
  setActiveTab: (tab) => set({ activeTab: tab }),
}));
