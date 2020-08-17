# NoteBook
实现多功能的记录本应用，包括便签功能（备忘录）、日记功能、纪念日功能、记账功能、笔记功能等

commit-1:添加侧滑抽屉栏，通过向右滑动能唤出抽屉进行切换，抽屉栏中添加了备忘录（memo）和日记（diary）选项，效果图见Pic/01.nav_bar.png

commit-2:添加备忘录基本逻辑，添加简易增加备忘录功能以及数据库存储

commit-3:修改备忘录列表布局，添加显示修改时间

commit-4:添加数据库操作抽象接口，完善数据库操作，完善备忘录功能的新增、删除、修改、查询

commit-11:将listView更换为RecyclerView，使用瀑布流StaggeredGridLayoutManager布局显示，添加自定义的item点击接口，实现点击进入文本编辑，
	长按删除的效果；更换item的背景为灰色边框黑色字体显示，同时调整布局边距。效果见Pic/commit-11.gif
