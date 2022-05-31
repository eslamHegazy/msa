import { CheckIcon, DotsVerticalIcon, TrashIcon } from '@radix-ui/react-icons';
import { ActionFunction, fetch, LoaderFunction } from '@remix-run/node';
import * as DropdownMenuPrimitive from '@radix-ui/react-dropdown-menu';
import {
  Form,
  useLoaderData,
  useSubmit,
  useTransition,
} from '@remix-run/react';
import { format, fromUnixTime } from 'date-fns';
import cx from 'classnames';

const USER = 'fake_user_2';
const JWT = 'fghjkhgf';

export const loader: LoaderFunction = async () => {
  const res = await fetch(`http://localhost:3000/getNotifications/${USER}`, {
    method: 'GET',
    headers: {
      Authorization: JWT,
      Command: 'getNotifications',
    },
  });
  return await res.json();
};

export const action: ActionFunction = async ({ request }) => {
  let formData = await request.formData();
  let { _action } = Object.fromEntries(formData);
  let notificationId = formData.get('notificationId');
  let userId = formData.get('userId');

  switch (_action) {
    case 'sendNotification':
      const sendNotificationResponse = await fetch(
        'http://localhost:3000/sendNotification',
        {
          body: JSON.stringify({
            title: 'test',
            body: 'helloooo',
            sender: 'fake_user_1',
            receivers: [USER],
          }),
          method: 'POST',
          headers: {
            Authorization: JWT,
            Command: 'sendNotification',
            'Content-Type': 'application/json;charset=UTF-8',
          },
        }
      );
      const test = await sendNotificationResponse.text();
      console.log(test);
      return test;

    case 'markAsRead':
      const markAsReadResponse = await fetch(
        'http://localhost:3000/markNotificationAsRead',
        {
          body: JSON.stringify({ notificationId, userId }),
          method: 'PUT',
          headers: {
            Authorization: JWT,
            Command: 'markNotificationAsRead',
            'Content-Type': 'application/json;charset=UTF-8',
          },
        }
      );
      return await markAsReadResponse.json();

    case 'delete':
      let deleteNotificationResponse = await fetch(
        'http://localhost:3000/deleteNotification',
        {
          method: 'DELETE',
          headers: {
            Authorization: JWT,
            Command: 'deleteNotification',
            'Content-Type': 'application/json;charset=UTF-8',
          },
          body: JSON.stringify({
            notificationId,
            userId,
          }),
        }
      );
      return await deleteNotificationResponse.json();
  }
};

const menuItems = [
  {
    label: 'Mark as read',
    icon: <CheckIcon className="mr-2 h-3.5 w-3.5" />,
    value: 'markAsRead',
  },
  {
    label: 'Delete',
    icon: <TrashIcon className="mr-2 h-3.5 w-3.5" />,
    value: 'delete',
  },
];

export default function Index() {
  const data = useLoaderData();
  const transition = useTransition();
  const submit = useSubmit();

  const handleChange = (e) => {
    submit(e.currentTarget);
  };

  if (!data) {
    return <p>Something went wrong, please try again</p>;
  }

  return (
    <div>
      <main>
        <section className="max-w-5xl mx-auto my-20 px-4 sm:px-6 lg:px-8">
          <div className="sm:flex sm:items-center mb-5">
            <div className="sm:flex-auto">
              <h1 className="text-xl font-semibold text-gray-900">
                Notifications
              </h1>
              <p className="mt-2 text-sm text-gray-700">
                Notifications returned by the Notifications microservice
              </p>
            </div>
            <Form method="post" className="mt-4 sm:mt-0 sm:ml-16 sm:flex-none">
              <button
                name="_action"
                value="sendNotification"
                className="inline-flex items-center justify-center rounded-md border border-transparent bg-blue-600 px-4 py-2 text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 sm:w-auto"
              >
                {transition.state === 'submitting' &&
                transition.submission.formData.get('_action') ===
                  'sendNotification'
                  ? 'Sending..'
                  : 'Send Notification'}
              </button>
            </Form>
          </div>
          <ul className="divide-y divide-gray-200">
            {data.body.map((notification: any) => (
              <li key={notification.id} className={`p-5 flex justify-between`}>
                <div
                  className={cx(
                    `space-y-2`,
                    transition.state === 'submitting' &&
                      transition.submission.formData.get('_action') ===
                        'delete' &&
                      transition.submission.formData.get('notificationId') ===
                        notification.id
                      ? 'opacity-25'
                      : ''
                  )}
                >
                  <div className="relative">
                    {!notification.read && (
                      <span className="w-2 h-2 bg-blue-500 rounded-full absolute top-3 -left-5"></span>
                    )}
                    <p className="text-xl font-medium">{notification.title}</p>
                  </div>
                  <p>{notification.body}</p>
                  <div className="flex flex-col text-sm md:flex-row space-y-4 md:space-y-0 md:space-x-4 md:items-center">
                    <p className="text-gray-500">{notification.sender}</p>
                    <p>
                      {format(
                        fromUnixTime(notification.timestamp / 1000),
                        `MMMM do yyyy 'at' h:mm a`
                      )}
                    </p>
                  </div>
                </div>
                <div className="relative">
                  <DropdownMenuPrimitive.Root key={notification.id}>
                    <DropdownMenuPrimitive.Trigger>
                      <DotsVerticalIcon className="w-5 h-5 text-gray-500 hover:text-gray-600" />
                    </DropdownMenuPrimitive.Trigger>
                    <DropdownMenuPrimitive.Content
                      align="end"
                      sideOffset={5}
                      className={cx(
                        '-mt-20 radix-side-top:animate-slide-up radix-side-bottom:animate-slide-down',
                        'w-48 rounded-lg px-1.5 py-1 shadow-md md:w-56',
                        'bg-white'
                      )}
                    >
                      {menuItems.map(({ icon, label, value }, i) => (
                        <Form
                          method={value === 'delete' ? 'delete' : 'put'}
                          onChange={handleChange}
                          key={label}
                          className={cx(
                            'flex relative  w-full items-center rounded-md px-2 py-2 text-xs outline-none hover:bg-gray-50',
                            'text-gray-700 focus:bg-gray-50'
                          )}
                        >
                          {icon}
                          <button
                            name="_action"
                            value={value}
                            className="flex-grow text-left text-gray-700"
                          >
                            {transition.state === 'submitting' &&
                              transition.submission.formData.get('_action') ===
                                value}{' '}
                            {label}
                          </button>
                          <input type="hidden" name="_action" value={value} />
                          <input
                            type="hidden"
                            name="notificationId"
                            value={notification.id}
                          />
                          <input type="hidden" name="userId" value={USER} />
                        </Form>
                      ))}
                    </DropdownMenuPrimitive.Content>
                  </DropdownMenuPrimitive.Root>
                </div>
              </li>
            ))}
          </ul>
        </section>
      </main>
    </div>
  );
}
