import MyGardenPage from 'pages/MyGardenPage/MyGardenPage';
import SettingsPage from 'pages/SettingsPage/SettingsPage';
import MyAlbumPage from 'pages/MyAlbumPage/MyAlbumPage';
import MyCabinetPage from 'pages/MyCabinetPage/MyCabinetPage';
import { useRoutes } from 'react-router';
import LandingPage from 'pages/LandingPage/LandingPage';
import MainPage from 'pages/MainPage/MainPage';
import ContentsCreatePage from 'pages/ContentsCreatePage/ContentsCreatePage';
import ContentsListPage from 'pages/ContentsListPage/ContentsListPage';
import LoginPage from 'pages/LandingPage/LoginPage';
import SignupPage from 'pages/LandingPage/SignupPage';
import MyGardenDandelionDetail from 'pages/MyGardenPage/MyDandelDetailPage';
import Background from 'layouts/background';
import GardenBackground from 'layouts/garden/gardenBack';
import MyDandelDetailPage from 'pages/MyGardenPage/MyDandelDetailPage';
import MyDandelionPlant from 'pages/MyGardenPage/MyDandelionPlant';
import MyDandelArrivedPage from 'pages/MyGardenPage/MyDandelArrivedPage';
import DesktopPage from 'pages/DesktopPage';
import GuidePage from 'pages/GuidePage';

export default function Router() {
  return useRoutes([
    {
      path: '/',
      element: <Background />,
      children: [{ path: '', element: <LandingPage /> }],
    },
    {
      path: '/main',
      element: <Background />,
      children: [{ element: <MainPage /> }],
    },
    {
      path: '/mygarden',
      element: <GardenBackground />,
      children: [
        { path: '', element: <MyGardenPage /> },
        { path: 'album', element: <MyAlbumPage /> },
      ],
    },
    {
      path: '/mygarden',
      children: [
        { path: 'plant/:id', element: <MyDandelionPlant /> },
        { path: 'arrived/:id', element: <MyDandelArrivedPage /> },
        { path: 'dandelions/:id', element: <MyGardenDandelionDetail /> },
      ],
    },
    {
      path: '/settings',
      element: <GardenBackground />,
      children: [{ path: '', element: <SettingsPage /> }],
    },
    {
      path: '/contents',
      element: <Background />,
      children: [
        { path: 'create', element: <ContentsCreatePage /> },
        { path: 'list', element: <ContentsListPage /> },
      ],
    },
    {
      path: '/login',
      element: <Background />,
      children: [{ path: '', element: <LoginPage /> }],
    },
    {
      path: '/signup',
      element: <SignupPage />,
      children: [{ path: '', element: <SignupPage /> }],
    },
    {
      path: '/layout',
      element: <Background />,
    },
    {
      path: '/desktop',
      element: <DesktopPage />,
    },
    {
      path: '/guide',
      element: <GuidePage />,
    },
    {
      path: '/test',
      element: <GardenBackground />,
    },
  ]);
}
